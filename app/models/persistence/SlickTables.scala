package models.persistence

import models.entities.Category
import models.entities.Product
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile

/**
  * The companion object.
  */
object SlickTables extends HasDatabaseConfig[JdbcProfile] {

  protected lazy val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import dbConfig.driver.api._

  abstract class BaseTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  }

  
  class CategoryTable(tag: Tag) extends BaseTable[Category](tag, "category") {
    def name = column[String]("name")
    def parentId = column[Long]("parentId")
    def * = (id, name, parentId) <> (Category.tupled, Category.unapply)
    
  }

  val categoryTableQ : TableQuery[CategoryTable] = TableQuery[CategoryTable]
  
  class ProductTable(tag: Tag) extends BaseTable[Product](tag, "product") {
  def name = column[String]("name")
  def categoryId = column[Long]("categoryId")
  def color = column[String]("color")
  def price = column[Double]("price")
  def size = column[String]("size")
  def * = (id, name, categoryId, color, price, size) <> (Product.tupled, Product.unapply)
  
  // A reified foreign key relation that can be navigated to create a join
  def category = foreignKey("Category_FK", categoryId, categoryTableQ)(_.id)
  }
  
  val productTableQ : TableQuery[ProductTable] = TableQuery[ProductTable]
  
  
}
