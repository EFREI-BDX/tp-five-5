use std::fmt;

#[derive(Debug, Clone, PartialEq, Eq)]
pub enum ApplicationError {
    DomainRuleViolation(String),
    Repository(String),
}

impl ApplicationError {
    pub fn domain(message: impl Into<String>) -> Self {
        Self::DomainRuleViolation(message.into())
    }

    pub fn repository(message: impl Into<String>) -> Self {
        Self::Repository(message.into())
    }
}

impl fmt::Display for ApplicationError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            ApplicationError::DomainRuleViolation(message) => write!(f, "{}", message),
            ApplicationError::Repository(message) => write!(f, "{}", message),
        }
    }
}

impl std::error::Error for ApplicationError {}

pub type ApplicationResult<T> = Result<T, ApplicationError>;
