use sea_orm::entity::prelude::*;

#[derive(Clone, Debug, PartialEq, DeriveEntityModel)]
#[sea_orm(table_name = "match_read_models")]
pub struct Model {
    #[sea_orm(primary_key, auto_increment = false)]
    pub match_id: String,
    pub summary: Json,
    pub player_stats: Json,
    pub calculated_at: String,
}

#[derive(Copy, Clone, Debug, EnumIter, DeriveRelation)]
pub enum Relation {}

impl ActiveModelBehavior for ActiveModel {}
