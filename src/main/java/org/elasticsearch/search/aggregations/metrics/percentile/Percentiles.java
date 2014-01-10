/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.search.aggregations.metrics.percentile;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.Aggregation;

import java.io.IOException;

/**
 *
 */
public interface Percentiles extends Aggregation, Iterable<Percentiles.Percentile> {

    public static abstract class ExecutionHint {

        public static ExecutionHint frugal() {
            return Frugal.instance;
        }

        public static TDigest tDigest() {
            return new TDigest();
        }

        public static QDigest qDigest() {
            return new QDigest();
        }

        private final String type;

        protected ExecutionHint(String type) {
            this.type = type;
        }

        private static class Frugal extends ExecutionHint {

            private final static Frugal instance = new Frugal();

            private Frugal() {
                super("frugal");
            }

            @Override
            void paramsToXContent(XContentBuilder builder) throws IOException {
            }
        }

        public static class TDigest extends ExecutionHint {

            protected double compression = -1;

            TDigest() {
                super("tdigest");
            }

            public TDigest compression(double compression) {
                this.compression = compression;
                return this;
            }

            @Override
            void paramsToXContent(XContentBuilder builder) throws IOException {
                if (compression > 0) {
                    builder.field("compression", compression);
                }
            }
        }

        public static class QDigest extends ExecutionHint {

            protected double compression = -1;

            QDigest() {
                super("qdigest");
            }

            public QDigest compression(double compression) {
                this.compression = compression;
                return this;
            }

            @Override
            void paramsToXContent(XContentBuilder builder) throws IOException {
                if (compression > 0) {
                    builder.field("compression", compression);
                }
            }
        }

        String type() {
            return type;
        }

        abstract void paramsToXContent(XContentBuilder builder) throws IOException;

    }

    public static interface Percentile {

        double getPercent();

        double getValue();

    }

    double percentile(double percent);

}
