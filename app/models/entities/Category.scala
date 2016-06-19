package models.entities

case class Category(id: Long, name: String, parentId: Long) extends BaseEntity