use crate::application::MatchQueryService;
use std::sync::Arc;

pub struct QueryHttpState<Q: MatchQueryService> {
    query_service: Arc<Q>,
}

impl<Q: MatchQueryService> QueryHttpState<Q> {
    pub fn new(query_service: Q) -> Self {
        Self {
            query_service: Arc::new(query_service),
        }
    }

    pub fn query_service(&self) -> &Q {
        &self.query_service
    }
}

impl<Q: MatchQueryService> Clone for QueryHttpState<Q> {
    fn clone(&self) -> Self {
        Self {
            query_service: Arc::clone(&self.query_service),
        }
    }
}
