#!/usr/bin/env python3
"""
Validate JSON fixtures in `tests/fixtures`.

Event fixtures are full transport envelopes, while event schemas describe the
payload only. This mirrors the Rust Consumer flow:
BaseEvent schema -> per-event payload schema.

Fixtures named `<event>.invalid.json` are expected to fail schema validation.
Fixtures such as `<event>.invalid-score.json` may still be schema-valid when
they target application-level rules instead of JSON Schema rules.
"""
import json
import sys
from pathlib import Path
from jsonschema import validate, ValidationError, RefResolver, FormatChecker

ROOT = Path(__file__).resolve().parents[1]
SCHEMA_DIR = ROOT / 'tests' / 'schemas'
FIXTURES_DIR = ROOT / 'tests' / 'fixtures'

def load_schema(path: Path):
    return json.loads(path.read_text())

def validate_fixture(data, schema, resolver):
    if isinstance(data, dict) and 'payload' in data and 'type' in data:
        base_schema = load_schema(SCHEMA_DIR / 'BaseEvent.schema.json')
        validate(instance=data, schema=base_schema, resolver=resolver, format_checker=FormatChecker())
        validate(instance=data['payload'], schema=schema, resolver=resolver, format_checker=FormatChecker())
    else:
        validate(instance=data, schema=schema, resolver=resolver, format_checker=FormatChecker())

def main():
    SCHEMA_DIR_ABS = str(SCHEMA_DIR.resolve())
    success = True
    for fixture in FIXTURES_DIR.glob('*.json'):
        base = fixture.stem  # e.g. match-summary.valid -> match-summary.valid
        # handle names like match-summary.valid.json -> baseParts
        parts = base.split('.')
        schema_base = parts[0]
        expect_schema_failure = len(parts) > 1 and parts[1] == 'invalid'
        schema_path = SCHEMA_DIR / f"{schema_base}.schema.json"
        if not schema_path.exists():
            print(f"SKIP: no schema for fixture {fixture.name} (expected {schema_path.name})")
            continue
        schema = load_schema(schema_path)
        if schema == {}:
            print(f"SKIP: empty placeholder schema for fixture {fixture.name}")
            continue
        data = json.loads(fixture.read_text())
        resolver = RefResolver(base_uri=f"file://{SCHEMA_DIR_ABS}/", referrer=schema)
        try:
            validate_fixture(data, schema, resolver)
            if expect_schema_failure:
                print(f"FAIL: {fixture.name} -> {schema_path.name}\n  expected schema validation to fail")
                success = False
            else:
                print(f"OK: {fixture.name} -> {schema_path.name}")
        except ValidationError as e:
            if expect_schema_failure:
                print(f"OK: {fixture.name} rejected by {schema_path.name}")
            else:
                print(f"FAIL: {fixture.name} -> {schema_path.name}\n  {e.message}")
                success = False
    return 0 if success else 2

if __name__ == '__main__':
    sys.exit(main())
