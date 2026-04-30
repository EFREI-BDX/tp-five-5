package org.efrei.five.apimanagematch.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private record StatusEntry(String code, String description) {}

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Manage Match")
                        .version("1.0.0")
                        .description("""
                                API de gestion des matchs de football.

                                Permet la création, la consultation et l'annulation de matchs.
                                À la création, deux timers sont automatiquement planifiés :
                                - à start_time : évènement "match started" (statut → IN_PROGRESS)
                                - à end_time   : évènement "match ended"   (statut → FINISHED)

                                Expose également des endpoints de consommation d'évènements
                                en provenance du service manage-team (mise à jour du cache local,
                                annulation des matchs sur dissolution d'équipe).
                                """));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            String controller = handlerMethod.getBeanType().getSimpleName();
            String method = handlerMethod.getMethod().getName();
            return switch (controller) {
                case "MatchController" -> customizeMatchController(operation, method);
                case "TeamEventsInboundController" -> customizeTeamEventsController(operation, method);
                default -> operation;
            };
        };
    }

    // -------------------------------------------------------------------------
    // MatchController
    // -------------------------------------------------------------------------

    private Operation customizeMatchController(Operation op, String method) {
        op.setTags(List.of("Matches"));
        return switch (method) {
            case "get" -> op
                    .summary("Récupérer un match par son ID")
                    .description("Retourne le détail complet d'un match : équipes, terrain, période et statut courant.")
                    .responses(responses(
                            r("200", "Match trouvé"),
                            r("404", "Match introuvable")));

            case "create" -> op
                    .summary("Créer un match")
                    .description("""
                            Crée un nouveau match entre deux équipes sur un terrain donné.

                            Deux timers sont automatiquement planifiés à la création :
                            - à start_time : mise à jour du statut en IN_PROGRESS + envoi de l'évènement "match started"
                            - à end_time   : mise à jour du statut en FINISHED + envoi de l'évènement "match ended"

                            Les URLs de destination sont configurables via :
                            match.outbound.notify-urls.started / match.outbound.notify-urls.ended
                            """)
                    .responses(responses(
                            r("200", "Match créé, timers planifiés"),
                            r("404", "Équipe ou terrain introuvable"),
                            r("409", "Conflit de réservation sur le terrain")));

            case "cancel" -> op
                    .summary("Annuler un match")
                    .description("""
                            Annule un match et met son statut à CANCELLED.

                            N'arrête pas les timers planifiés — l'annulation des timers
                            est déclenchée par l'évènement de dissolution d'équipe
                            (POST /api/inbounds/teams/dissolved).
                            """)
                    .responses(responses(
                            r("200", "Match annulé"),
                            r("404", "Match introuvable")));

            default -> op;
        };
    }

    // -------------------------------------------------------------------------
    // TeamEventsInboundController
    // -------------------------------------------------------------------------

    private Operation customizeTeamEventsController(Operation op, String method) {
        op.setTags(List.of("Team Events (Inbound)"));
        return switch (method) {
            case "teamUpdated" -> op
                    .summary("Évènement : équipe mise à jour")
                    .description("""
                            Consommé par manage-team lorsqu'une équipe change de label, de tag ou de leader.

                            Met à jour le cache local de l'équipe (upsert dans la table teams).
                            """)
                    .responses(responses(
                            r("200", "Cache local mis à jour")));

            case "teamDissolved" -> op
                    .summary("Évènement : équipe dissoute")
                    .description("""
                            Consommé par manage-team lorsqu'une équipe est dissoute.

                            Pour chaque match NOT_STARTED impliquant cette équipe :
                            - annule le timer planifié (l'évènement "match started" ne sera pas déclenché)
                            - met le statut du match à CANCELLED
                            """)
                    .responses(responses(
                            r("200", "Matchs annulés, timers arrêtés")));

            default -> op;
        };
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private ApiResponses responses(StatusEntry... entries) {
        ApiResponses apiResponses = new ApiResponses();
        for (StatusEntry e : entries) {
            apiResponses.addApiResponse(e.code(), new ApiResponse().description(e.description()));
        }
        return apiResponses;
    }

    private StatusEntry r(String code, String description) {
        return new StatusEntry(code, description);
    }
}