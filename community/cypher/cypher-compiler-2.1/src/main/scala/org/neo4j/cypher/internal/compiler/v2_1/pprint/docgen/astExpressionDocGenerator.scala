/**
 * Copyright (c) 2002-2014 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compiler.v2_1.pprint.docgen

import org.neo4j.cypher.internal.compiler.v2_1.pprint.{Doc, RecursiveDocGenerator, NestedDocGenerator}
import org.neo4j.cypher.internal.compiler.v2_1.ast._
import org.neo4j.cypher.internal.compiler.v2_1.ast.Equals
import org.neo4j.cypher.internal.compiler.v2_1.ast.Property

case object astExpressionDocGenerator extends NestedDocGenerator[Any] {

  import Doc._

  protected val instance: RecursiveDocGenerator[Any] = {
    case Identifier(name) => (inner) =>
      text(name)

    case (Equals(left, right)) => (inner) =>
      inner(left) :/: "=" :/: inner(right)

    case (Property(map, PropertyKeyName(name))) => (inner) =>
      inner(map) :: "." :: name
  }
}
