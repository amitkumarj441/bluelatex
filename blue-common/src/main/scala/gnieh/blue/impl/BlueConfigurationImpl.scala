/*
 * This file is part of the \BlueLaTeX project.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gnieh.blue
package impl

import java.util.Properties

import gnieh.sohva.sync._

import com.typesafe.config._

import scala.collection.JavaConverters._

import java.io.{
  File,
  FileNotFoundException
}

import org.fusesource.scalate._
import org.fusesource.scalate.util._

class BlueConfiguration(conf: Config) {

  // ===== blue core settings =====

  val blue = conf.getConfig("blue")

  val recaptchaPrivateKey = optionalString("recaptcha.private-key")

  // ===== couchdb settings =====

  val couch = new CouchConfigurationImpl(conf.getConfig("couchdb"))

  // ===== email settings =====

  val emailConf = {
    val props = new Properties
    for(entry <- conf.getConfig("mail").entrySet.asScala) {
      props.setProperty("mail." + entry.getKey, entry.getValue.render)
    }
    props
  }

  // ===== reset tokens =====

  val resetTokenValidity =
    conf.getLong("blue.reset.validity")

  // ===== templates =====

  val templateDir =
    new File("blue.template.directory")

  // ===== directories =====

  /** The paper directory associated with the paper identifier */
  def paperDir(paperId: String) =
    new File(papers, paperId)

  val baseUrl =
    conf.getString("blue.url")

  // ===== internals =====

  private val papers: File =
    new File(conf.getString("blue.paper"))

  private def optionalString(path: String): Option[String] =
    if(conf.hasPath(path))
      Some(conf.getString(path))
    else
      None

}
