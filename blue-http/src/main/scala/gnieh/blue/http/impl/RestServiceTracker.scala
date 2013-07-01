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
package http
package impl

import tiscaf._

import org.osgi.framework._
import org.osgi.util.tracker.ServiceTracker

import scala.collection.mutable.Map

/** Tracks the `RestHandler` services that are (un)registered to dynamically extend
 *  the server interface
 *
 *  @author Lucas Satabin
 */
class RestServiceTracker(context: BundleContext, server: BlueServer)
  extends ServiceTracker[RestApplication, Long](context, classOf[RestApplication], null) {

  override def addingService(ref: ServiceReference[RestApplication]): Long = {
    // get the service instance
    val service = context.getService(ref)
    // get the servive identifier
    val id = ref.getProperty(Constants.SERVICE_ID).asInstanceOf[Long]
    synchronized {
      // register the rest interface with the generated identifier
      server.appMap(id) = service
    }
    id
  }

  override def removedService(ref: ServiceReference[RestApplication], id: Long): Unit = synchronized {
    // unregister the handlers for the given identifier
    server.appMap -= id
  }

}
