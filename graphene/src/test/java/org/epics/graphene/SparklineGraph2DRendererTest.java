/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.util.Random;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;


public class SparklineGraph2DRendererTest {
    
    public SparklineGraph2DRendererTest() {
    }

    private static Point2DDataset largeDataset;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Random rand = new Random(1);
        int nSamples = 100000;
        double[] waveform = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            waveform[i] = rand.nextGaussian();
        }
        largeDataset = org.epics.graphene.Point2DDatasets.lineData(waveform);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        largeDataset = null;
    }
    
/*    @Test
    public void test1() throws Exception {
        double[] initialDataX = new double[100];
        for(int i = 0; i< 100; i++)
            {
                initialDataX[i] = i;
            }
        Point2DDataset data = Point2DDatasets.lineData(initialDataX);
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(640, 100, "Pounds");
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, data);
        ImageAssert.compareImages("sparkline2D.1", image);
    }*/
    @Test
    public void testGetMaxIndex() throws Exception{
//        double[] raw_data = {0,5,15,20,15,5,0};
//        Point2DDataset data = Point2DDatasets.lineData(raw_data);
//        SparklineGraph2DRenderer graph_object = new SparklineGraph2DRenderer(100, 100);
//        assertEquals(3, graph_object.getMaxIndex());
    }
    @Test
    public void testGetMinIndex() throws Exception{
//        double[] raw_data = {20,5,10,0,0,0,5};
//        Point2DDataset data = Point2DDatasets.lineData(raw_data);
//        SparklineGraph2DRenderer graph_object = new SparklineGraph2DRenderer(100, 100);
//        assertEquals(3, graph_object.getMinIndex());
    }
}
