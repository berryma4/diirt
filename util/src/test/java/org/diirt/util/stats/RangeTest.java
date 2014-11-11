/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.stats;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class RangeTest {
    
    @Test
    public void range1() throws Exception {
        Range range = Range.create(0.0, 10.0);
        assertThat(range.getMinimum(), equalTo(0.0));
        assertThat(range.getMaximum(), equalTo(10.0));
        assertThat(range.isReversed(), equalTo(false));
    }
    
    public void range2() throws Exception {
        Range range = Range.create(0.0, 0.0);
        assertThat(range.getMinimum(), equalTo(0.0));
        assertThat(range.getMaximum(), equalTo(0.0));
        assertThat(range.isReversed(), equalTo(false));
    }
    
    @Test
    public void range3() throws Exception {
        Range range = Range.create(10.0, 0.0);
        assertThat(range.getMinimum(), equalTo(0.0));
        assertThat(range.getMaximum(), equalTo(10.0));
        assertThat(range.isReversed(), equalTo(true));
    }
    
}
