/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.impl.entity;

import org.apache.http.HttpMessage;
import org.apache.http.ProtocolException;
import org.apache.http.entity.ContentLengthStrategy;
import org.junit.Assert;
import org.junit.Test;

public class TestStrictContentLengthStrategy {

    @Test
    public void testEntityWithChunkTransferEncoding() throws Exception {
        final ContentLengthStrategy lenStrategy = new StrictContentLengthStrategy();
        final HttpMessage message = new DummyHttpMessage();
        message.addHeader("Transfer-Encoding", "Chunked");

        Assert.assertEquals(ContentLengthStrategy.CHUNKED, lenStrategy.determineLength(message));
    }

    @Test
    public void testEntityWithIdentityTransferEncoding() throws Exception {
        final ContentLengthStrategy lenStrategy = new StrictContentLengthStrategy();
        final HttpMessage message = new DummyHttpMessage();
        message.addHeader("Transfer-Encoding", "Identity");

        Assert.assertEquals(ContentLengthStrategy.IDENTITY, lenStrategy.determineLength(message));
    }

    @Test(expected=ProtocolException.class)
    public void testEntityWithInvalidTransferEncoding() throws Exception {
        final ContentLengthStrategy lenStrategy = new StrictContentLengthStrategy();
        final HttpMessage message = new DummyHttpMessage();
        message.addHeader("Transfer-Encoding", "whatever");
        lenStrategy.determineLength(message);
    }

    @Test
    public void testEntityWithContentLength() throws Exception {
        final ContentLengthStrategy lenStrategy = new StrictContentLengthStrategy();
        final HttpMessage message = new DummyHttpMessage();
        message.addHeader("Content-Length", "100");
        Assert.assertEquals(100, lenStrategy.determineLength(message));
    }

    @Test(expected=ProtocolException.class)
    public void testEntityWithInvalidContentLength() throws Exception {
        final ContentLengthStrategy lenStrategy = new StrictContentLengthStrategy();
        final HttpMessage message = new DummyHttpMessage();
        message.addHeader("Content-Length", "whatever");
        lenStrategy.determineLength(message);
    }

    @Test(expected=ProtocolException.class)
    public void testEntityWithNegativeContentLength() throws Exception {
        final ContentLengthStrategy lenStrategy = new StrictContentLengthStrategy();
        final HttpMessage message = new DummyHttpMessage();
        message.addHeader("Content-Length", "-10");
        lenStrategy.determineLength(message);
    }

    @Test
    public void testEntityNoContentDelimiter() throws Exception {
        final ContentLengthStrategy lenStrategy = new StrictContentLengthStrategy();
        final HttpMessage message = new DummyHttpMessage();
        Assert.assertEquals(ContentLengthStrategy.UNDEFINED, lenStrategy.determineLength(message));
    }

}

