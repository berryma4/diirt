/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.util.Objects;
import org.epics.util.stats.Range;
import org.epics.util.stats.Ranges;

/**
 * TODO: finalize names
 *
 * @author carcassi
 */
public class AxisRanges {
    
    private AxisRanges() {
    }
    
    public static AxisRange fixed(final double min, final double max) {
        final Range fixedRange = Ranges.range(min, max);
        return new Fixed(fixedRange);
    }
    
    public static class Fixed implements AxisRange {
        
        private final AxisRange axisRange = this;
        private final Range absoluteRange;

        private Fixed(Range absoluteRange) {
            this.absoluteRange = absoluteRange;
        }

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                        return absoluteRange;
                }

                @Override
                public AxisRange getAxisRange() {
                    return axisRange;
                }
            };
        }

        @Override
        public String toString() {
            return "absolute(" + absoluteRange.getMinimum() + ", " + absoluteRange.getMaximum() + ")";
        }

        public Range getAbsoluteRange() {
            return absoluteRange;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Fixed) {
                return Ranges.equals(getAbsoluteRange(), ((Fixed) obj).getAbsoluteRange());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.absoluteRange);
            return hash;
        }
        
    }
    
    public static AxisRange data() {
        return DATA;
    }
    
    private static Data DATA = new Data();
    
    public static class Data implements AxisRange {
        
        private final AxisRange axisRange = this;

        private Data() {
        }

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                    return dataRange;
                }

                @Override
                public AxisRange getAxisRange() {
                    return axisRange;
                }
            };
        }

        @Override
        public String toString() {
            return "data";
        }
    }
    
    public static AxisRange integrated() {
        return INTEGRATED;
    }
    
    public static AxisRange integrated(double minUsage) {
        return new Integrated(minUsage);
    }
    
    private static final Integrated INTEGRATED = new Integrated(0.8);
    
    public static class Integrated implements AxisRange {

        private final AxisRange axisRange = this;
        private final double minUsage;

        private Integrated(double minUsage) {
            this.minUsage = minUsage;
        }

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {

                private Range aggregatedRange;

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                    aggregatedRange = Ranges.aggregateRange(dataRange, aggregatedRange);
                    if (Ranges.overlap(aggregatedRange, dataRange) < minUsage) {
                        aggregatedRange = dataRange;
                    }
                    return aggregatedRange;
                }

                @Override
                public AxisRange getAxisRange() {
                    return axisRange;
                }
            };
        }

        @Override
        public String toString() {
            return "integrated(" + (int) (minUsage * 100) + "%)";
        }

        public double getMinUsage() {
            return minUsage;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Integrated) {
                return getMinUsage() == ((Integrated) obj).getMinUsage();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.getMinUsage());
            return hash;
        }
        
    }
    
    public static AxisRange display() {
        return DISPLAY;
    }
    
    private static final Display DISPLAY = new Display();
    
    public static class Display implements AxisRange {
            
        private final AxisRange axisRange = this;

        private Display() {
        }

        @Override
        public AxisRangeInstance createInstance() {
            return new AxisRangeInstance() {
                
                private Range previousDataRange;

                @Override
                public Range axisRange(Range dataRange, Range displayRange) {
                    if (Ranges.isValid(displayRange)) {
                        return displayRange;
                    } else if (previousDataRange == null) {
                        previousDataRange = dataRange;
                        return previousDataRange;
                    } else {
                        return previousDataRange;
                    }
                }

                @Override
                public AxisRange getAxisRange() {
                    return axisRange;
                }
            };
        }

        @Override
        public String toString() {
            return "display";
        }
    }
}
