use crate::application::ApplicationService;
use crate::infrastructure::inbound::consumer::Consumer;
use std::sync::Arc;

pub struct EventHttpState<S: ApplicationService> {
    consumer: Arc<Consumer<S>>,
}

impl<S: ApplicationService> EventHttpState<S> {
    pub fn new(consumer: Consumer<S>) -> Self {
        Self {
            consumer: Arc::new(consumer),
        }
    }

    pub fn consumer(&self) -> &Consumer<S> {
        &self.consumer
    }
}

impl<S: ApplicationService> Clone for EventHttpState<S> {
    fn clone(&self) -> Self {
        Self {
            consumer: Arc::clone(&self.consumer),
        }
    }
}
