#!/usr/bin/env sh
set -eu

mariadb -uroot -p"${MARIADB_ROOT_PASSWORD}" <<EOSQL
REVOKE ALL PRIVILEGES, GRANT OPTION FROM '${MARIADB_USER}'@'%';

GRANT SELECT ON \`${MARIADB_DATABASE}\`.vw_field_statuses TO '${MARIADB_USER}'@'%';
GRANT SELECT ON \`${MARIADB_DATABASE}\`.vw_reservation_statuses TO '${MARIADB_USER}'@'%';
GRANT SELECT ON \`${MARIADB_DATABASE}\`.vw_fields TO '${MARIADB_USER}'@'%';
GRANT SELECT ON \`${MARIADB_DATABASE}\`.vw_active_fields TO '${MARIADB_USER}'@'%';
GRANT SELECT ON \`${MARIADB_DATABASE}\`.vw_blocking_reservations TO '${MARIADB_USER}'@'%';

GRANT EXECUTE ON PROCEDURE \`${MARIADB_DATABASE}\`.sp_update_field_status TO '${MARIADB_USER}'@'%';
GRANT EXECUTE ON PROCEDURE \`${MARIADB_DATABASE}\`.sp_create_reservation TO '${MARIADB_USER}'@'%';
GRANT EXECUTE ON PROCEDURE \`${MARIADB_DATABASE}\`.sp_update_reservation_status TO '${MARIADB_USER}'@'%';

FLUSH PRIVILEGES;
EOSQL
