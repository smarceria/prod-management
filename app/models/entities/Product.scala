package models.entities

case class Product(id: Long, name: String, categoryId: Long, color: String, price: Double, size: String) extends BaseEntity