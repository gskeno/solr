/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.cluster.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.solr.common.MapWriter;
import org.apache.solr.common.cloud.Replica;

/**
 * A plain interface that captures all inputs for a Solr request. As the name suggests, it lets
 * users interact with Solr using raw bytes and params instead of concrete objects.
 *
 * @param <T> The concrete return type object
 */
public interface RawRequest<T> {

  /** Use /solr or /api end points */
  RawRequest<T> withApiType(ApiType apiType);

  /**
   * Make a request to a specific Solr node
   *
   * @param node node name
   */
  RawRequest<T> withNode(String node);

  /**
   * Make a request to a specific collection
   *
   * @param collection collection name
   */
  RawRequest<T> withCollection(String collection);

  /**
   * Make a request to a specific shard and replica type. Ensure that the collection name is
   * supplied as well
   *
   * @param shardName shard name
   * @param replicaType type of replica
   */
  RawRequest<T> withShard(String shardName, Replica.Type replicaType);

  /**
   * If the shard name is not known , use a shard routing key (such as id). Ensure that the
   * collection name is supplied as well
   *
   * @param routingKey The key from which the hash can be derived for hash based router
   * @param replicaType The type of the replica
   */
  RawRequest<T> withShardRoute(String routingKey, Replica.Type replicaType);

  /**
   * Make a direct request to a specific replica of a collection. Ensure that the collection name is
   * supplied as well
   *
   * @param replicaName Name of he replica
   */
  RawRequest<T> withReplica(String replicaName);

  /**
   * The path to which the request needs to be made eg: /update , /admin/metrics etc. The actual
   * path depends on whether a collection is specified or not
   *
   * @param path The path
   */
  RawRequest<T> withPath(String path);

  /** The request parameters */
  RawRequest<T> withParams(MapWriter params);

  /** If there is a payload, write it directly to the server */
  RawRequest<T> withPayload(Consumer<OutputStream> os);

  /**
   * Parse and deserialize a concrete object . If this is not supplied, the response is just eaten
   * up and thrown away
   *
   * @param parser This impl should consume an input stream and return an object
   */
  RawRequest<T> withParser(Function<InputStream, T> parser);

  /**
   * do an HTTP GET operation
   *
   * @return the parsed object as returned by the parser. A null is returned if there is no parser
   *     set.
   */
  T GET();

  /**
   * do an HTTP POST operation
   *
   * @return the parsed object as returned by the parser. A null is returned if there is no parser
   *     set.
   */
  T POST();

  /**
   * do an HTTP DELETE operation
   *
   * @return the parsed object as returned by the parser. A null is returned if there is no parser
   *     set.
   */
  T DELETE();

  /**
   * do an HTTP PUT operation
   *
   * @return the parsed object as returned by the parser. A null is returned if there is no parser
   *     set.
   */
  T PUT();
}
