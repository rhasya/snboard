package models

import anorm._

class DVO {
  def getParam() = {
    val defFields = this.getClass.getDeclaredFields()
    defFields.map { field =>
      field.setAccessible(true)
      field.getName() -> toParameterValue(field.get(this))
    }.toSeq
  }
}