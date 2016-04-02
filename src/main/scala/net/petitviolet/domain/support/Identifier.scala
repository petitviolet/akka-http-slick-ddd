package net.petitviolet.domain.support

import java.util.UUID

trait Identifier[+A] {
  def value: A

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: Identifier[_] => that.value == this.value
    case _ => false
  }
}

object Identifier {
  def generate: String = UUID.randomUUID().toString
}