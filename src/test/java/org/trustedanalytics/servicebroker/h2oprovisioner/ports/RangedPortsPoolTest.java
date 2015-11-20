/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.trustedanalytics.servicebroker.h2oprovisioner.ports;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RangedPortsPoolTest {

    @Mock
    private PortChecker portChecker;

    @Test
    public void create_validPortNumbers_objectCreated() {
        new RangedPortsPool(portChecker, 8080, 8090);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_invalidPortNumbers1_exceptionThrown() {
        new RangedPortsPool(portChecker, 0, 8090);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_invalidPortNumbers2_exceptionThrown() {
        new RangedPortsPool(portChecker, 65536, 8090);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_invalidPortNumbers3_exceptionThrown() {
        new RangedPortsPool(portChecker, 8080, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_invalidPortNumbers4_exceptionThrown() {
        new RangedPortsPool(portChecker, 8080, 65536);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_invalidPortNumbers5_exceptionThrown() {
        new RangedPortsPool(portChecker, 1000, 100);
    }

    @Test
    public void getPort_allPortsAvailable_returnsPortsOneAfterAnother() {
        RangedPortsPool portsPool = new RangedPortsPool(allPortsAvailable(), 1, 3);
        assertThat(portsPool.getPort(), equalTo(1));
        assertThat(portsPool.getPort(), equalTo(2));
        assertThat(portsPool.getPort(), equalTo(3));
        assertThat(portsPool.getPort(), equalTo(1));
        assertThat(portsPool.getPort(), equalTo(2));
        assertThat(portsPool.getPort(), equalTo(3));
        assertThat(portsPool.getPort(), equalTo(1));
        assertThat(portsPool.getPort(), equalTo(2));
        assertThat(portsPool.getPort(), equalTo(3));
    }

    private PortChecker allPortsAvailable() {
        return port -> true;
    }

    @Test
    public void getPort_oddPortsAvailable_returnsOddPortsOneAfterAnother() {
        RangedPortsPool portsPool = new RangedPortsPool(onlyOddPortsAvailable(), 1, 4);
        assertThat(portsPool.getPort(), equalTo(1));
        assertThat(portsPool.getPort(), equalTo(3));
        assertThat(portsPool.getPort(), equalTo(1));
        assertThat(portsPool.getPort(), equalTo(3));
        assertThat(portsPool.getPort(), equalTo(1));
        assertThat(portsPool.getPort(), equalTo(3));
    }

    private PortChecker onlyOddPortsAvailable() {
        return port -> port % 2 != 0;
    }
}
