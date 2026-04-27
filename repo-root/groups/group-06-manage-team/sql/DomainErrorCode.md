# Codes d’erreur métier – DomainErrorCode

Ce fichier liste tous les codes d’erreur courts utilisés dans les messages des procédures stockées, avec leur
signification et la correspondance avec l’énumération Java `DomainErrorCode`.

| Code  | Enum                                  | Signification                                                               |
|-------|---------------------------------------|-----------------------------------------------------------------------------|
| TAIEX | TEAM_ALREADY_EXISTS                   | Une équipe avec cet identifiant existe déjà                                 |
| TALXT | TEAM_ALREADY_EXISTS                   | Une équipe avec ce label existe déjà                                        |
| TATXT | TEAM_ALREADY_EXISTS                   | Une équipe avec ce tag existe déjà                                          |
| TNFND | TEAM_NOT_FOUND                        | Aucune équipe avec cet identifiant                                          |
| TADIS | TEAM_ALREADY_DISSOLVED                | L’équipe est déjà dissoute                                                  |
| TNDIS | TEAM_NOT_DISSOLVED                    | L’équipe n’est pas dissoute                                                 |
| TLEMP | TEAM_LABEL_EMPTY                      | Le label de l’équipe ne doit pas être vide                                  |
| TTEEM | TEAM_TAG_EMPTY                        | Le tag de l’équipe ne doit pas être vide                                    |
| TCEMP | TEAM_CREATION_DATE_EMPTY              | La date de création ne doit pas être vide                                   |
| TDBFC | TEAM_DISSOLUTION_DATE_BEFORE_CREATION | La date de dissolution doit être postérieure ou égale à la date de création |
| TLINV | TEAM_LEADER_INVALID                   | Le leader doit être un joueur existant                                      |
| TLNMB | TEAM_LEADER_NOT_MEMBER                | Le leader doit être membre de l’équipe                                      |
| TTMPY | TEAM_TOO_MANY_PLAYERS                 | Une équipe ne peut pas avoir plus de 8 joueurs                              |
| PAEXT | PLAYER_ALREADY_EXISTS                 | Un joueur avec cet identifiant existe déjà                                  |
| PNFND | PLAYER_NOT_FOUND                      | Aucun joueur avec cet identifiant                                           |
| PDEMP | PLAYER_DISPLAY_NAME_EMPTY             | Le nom affiché du joueur ne doit pas être vide                              |
| PITLD | PLAYER_IS_TEAM_LEADER                 | Le joueur est déjà leader d’une équipe                                      |
| TFULL | TEAM_FULL                             | L’équipe a déjà 8 joueurs                                                   |
| PJEMP | PLAYERS_JSON_EMPTY                    | Le JSON de joueurs doit contenir au moins un joueur                         |

**Utilisation dans les messages d’erreur SQL:**