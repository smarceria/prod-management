package controllers

import javax.inject._
import models.daos.{AbstractBaseDAO, BaseDAO}
import models.entities.Product
import models.persistence.SlickTables.ProductTable
import play.api.libs.json.{Json, Writes}
import play.api.mvc._
import scala.concurrent.{Future, ExecutionContext}

class ProductController @Inject()(productDAO : AbstractBaseDAO[ProductTable,Product])(implicit exec: ExecutionContext) extends Controller {

  implicit val productWrites = new Writes[Product] {
    def writes(prod : Product) = Json.obj(
      "id" -> prod.id,
      "name" -> prod.name,
      "categoryId" -> prod.categoryId,
      "color" -> prod.color,
      "price" -> prod.price,
      "size" -> prod.size
      
    )
  }

  def index = Action.async {
    productDAO.findAll map { prod => Ok(Json.toJson(prod)) }
  }
    
  def product(id : Long) = Action.async {
    productDAO.findById(id) map { prod => prod.fold(NoContent)(prod => Ok(Json.toJson(prod))) }
  }

  def insertProduct = Action.async(parse.json) {
    request => {
      {
        for {
          name <- (request.body \ "name").asOpt[String]
          categoryId <- (request.body \ "categoryId").asOpt[Int]
          color <- (request.body \ "color").asOpt[String]
          price <- (request.body \ "price").asOpt[Double]
          size <- (request.body \ "size").asOpt[String]
          
        } yield {
          productDAO.insert(Product(0, name, categoryId, color, price, size)) map { n => Ok("Id of Product Added : " + n) }
        }
      }.getOrElse(Future{BadRequest("Wrong json format")})
    }
  }

   def delete(id: Long) = Action.async {
     productDAO.deleteById(id) map { n => Ok("Id of Product Deleted : " + n)}

  }
   
}
