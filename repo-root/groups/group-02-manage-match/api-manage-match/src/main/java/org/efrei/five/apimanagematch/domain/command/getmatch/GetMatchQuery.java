package org.efrei.five.apimanagematch.domain.command.getmatch;


import org.efrei.five.apimanagematch.domain.command.Query;

import java.util.UUID;


public record GetMatchQuery(UUID id) implements
        Query<GetMatchResponse> {
}


