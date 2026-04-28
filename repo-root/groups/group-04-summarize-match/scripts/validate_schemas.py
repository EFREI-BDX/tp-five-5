#!/usr/bin/env python3
"""
Validate all JSON fixtures in `tests/fixtures` against their corresponding
`tests/schemas/<name>.schema.json` files. Exits with non-zero on failure.
"""
import json
import sys
from pathlib import Path
from jsonschema import validate, ValidationError, RefResolver

ROOT = Path(__file__).resolve().parents[1]
SCHEMA_DIR = ROOT / 'tests' / 'schemas'
FIXTURES_DIR = ROOT / 'tests' / 'fixtures'

def load_schema(path: Path):
    return json.loads(path.read_text())

def main():
    SCHEMA_DIR_ABS = str(SCHEMA_DIR.resolve())
    success = True
    for fixture in FIXTURES_DIR.glob('*.json'):
        base = fixture.stem  # e.g. player-data.valid -> player-data.valid => want player-data
        # handle names like player-data.valid.json -> baseParts
        parts = base.split('.')
        schema_base = parts[0]
        schema_path = SCHEMA_DIR / f"{schema_base}.schema.json"
        if not schema_path.exists():
            print(f"SKIP: no schema for fixture {fixture.name} (expected {schema_path.name})")
            continue
        schema = load_schema(schema_path)
        data = json.loads(fixture.read_text())
        resolver = RefResolver(base_uri=f"file://{SCHEMA_DIR_ABS}/", referrer=schema)
        try:
            validate(instance=data, schema=schema, resolver=resolver)
            print(f"OK: {fixture.name} -> {schema_path.name}")
        except ValidationError as e:
            print(f"FAIL: {fixture.name} -> {schema_path.name}\n  {e.message}")
            success = False
    return 0 if success else 2

if __name__ == '__main__':
    sys.exit(main())
