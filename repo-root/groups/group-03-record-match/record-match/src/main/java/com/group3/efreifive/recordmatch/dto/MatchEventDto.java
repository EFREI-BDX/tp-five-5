package com.group3.efreifive.recordmatch.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour représenter un événement de match, incluant les détails du match, de l'événement, des joueurs impliqués, et d'autres informations pertinentes.
 * 
 * On avait deux options pour la gestions des informations supplémentaire en fonction de l'événement :
 * - Soit on ajoute des champs spécifiques pour chaque type d'événement (ex: team1Id pour un événement de type "score", referenceEventId pour un événement de type "foul", etc.)
 * - Soit on utilise une approche plus générique avec un champ "details" de type Map<String, Object> 
 * 
 * Dans ce cas, on a opté pour la première option pour plus de clarté et de simplicité
 */
public record MatchEventDto(
        UUID matchEventId,
        UUID matchId,
        UUID eventId,
        UUID player1Id,
        UUID player2Id,
        LocalDateTime occuredAt,
        Boolean succeeded, 
        UUID teamId,
        UUID referenceEventId
        
) {
}
