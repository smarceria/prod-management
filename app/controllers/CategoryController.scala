package controllers

import javax.inject._
import models.daos.{AbstractBaseDAO, BaseDAO}
import models.entities.Category
import models.persistence.SlickTables.CategoryTable
import play.api.libs.json.{Json, Writes}
import play.api.mvc._
import scala.concurrent.{Future, ExecutionContext}


class CategoryController @Inject()(categoryDAO : AbstractBaseDAO[CategoryTable, Category])(implicit exec: ExecutionContext) extends Controller {

  implicit val categoryWrites = new Writes[Category] {
    def writes(c: Category) = Json.obj(
      "id" -> c.id,
      "name" -> c.name,
      "parentId" -> c.parentId
    )
  }

  def index = Action.async {
    categoryDAO.findAll map { cats => Ok(Json.toJson(cats)) }
  }
    
  def category(id : Long) = Action.async {
    categoryDAO.findById(id) map { test => test.fold(NoContent)(cat => Ok(Json.toJson(cat))) }
  }

  def insertCategory = Action.async(parse.json) {
    request => {
      {
        for {
          name <- (request.body \ "name").asOpt[String]
          parentId <- (request.body \ "parentId").asOpt[Long]
        } yield {
          categoryDAO.insert(Category(0, name, parentId)) map { n => Ok("Id of Category Added : " + n) }
        }
      }.getOrElse(Future{BadRequest("Wrong json format")})
    }
  }

  def delete(id: Long) = Action.async {
     categoryDAO.deleteById(id) map { n => Ok("Id of Category Deleted : " + n)}

  }


}