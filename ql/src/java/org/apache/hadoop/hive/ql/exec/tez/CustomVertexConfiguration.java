/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.ql.exec.tez;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hive.ql.plan.TezWork.VertexType;
import org.apache.hadoop.io.Writable;

/*
 * This class is the payload for custom vertex. It serializes and de-serializes
 * @numBuckets: the number of buckets of the "big table"
 * @vertexType: this is the type of vertex and differentiates between bucket map join and SMB joins
 * @inputName: This is the name of the input. Used in case of SMB joins
 */
public class CustomVertexConfiguration implements Writable {

  private int numBuckets;
  private VertexType vertexType = VertexType.AUTO_INITIALIZED_EDGES;
  private String inputName;

  public CustomVertexConfiguration() {
  }

  public CustomVertexConfiguration(int numBuckets, VertexType vertexType, String inputName) {
    this.numBuckets = numBuckets;
    this.vertexType = vertexType;
    this.inputName = inputName;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(this.vertexType.ordinal());
    out.writeInt(this.numBuckets);
    out.writeUTF(inputName);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    this.vertexType = VertexType.values()[in.readInt()];
    this.numBuckets = in.readInt();
    this.inputName = in.readUTF();
  }

  public int getNumBuckets() {
    return numBuckets;
  }

  public VertexType getVertexType() {
    return vertexType;
  }

  public String getInputName() {
    return inputName;
  }
}