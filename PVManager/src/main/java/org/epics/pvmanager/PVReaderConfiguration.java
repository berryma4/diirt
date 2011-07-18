/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.concurrent.Executor;
import org.epics.pvmanager.util.TimeDuration;

/**
 * An expression used to set the final parameters on how the pv expression
 * should be monitored.
 * @param <T> the type of the expression
 * @author carcassi
 */
public class PVReaderConfiguration<T> extends CommonConfiguration {

    @Override
    public PVReaderConfiguration<T> from(DataSource dataSource) {
        super.from(dataSource);
        return this;
    }

    @Override
    public PVReaderConfiguration<T> notifyOn(Executor onThread) {
        super.notifyOn(onThread);
        return this;
    }
    private DesiredRateExpression<T> aggregatedPVExpression;
    private ExceptionHandler exceptionHandler;

    PVReaderConfiguration(DesiredRateExpression<T> aggregatedPVExpression) {
        this.aggregatedPVExpression = aggregatedPVExpression;
    }

    /**
     * Forwards exception to the given exception handler. No thread switch
     * is done, so the handler is notified on the thread where the exception
     * was thrown.
     * <p>
     * Giving a custom exception handler will disable the default handler,
     * so {@link PVReader#lastException() } is no longer set and no notification
     * is done.
     *
     * @param exceptionHandler an exception handler
     * @return this
     */
    public PVReaderConfiguration<T> routeExceptionsTo(ExceptionHandler exceptionHandler) {
        if (this.exceptionHandler != null) {
            throw new IllegalArgumentException("Exception handler already set");
        }
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * Sets the rate of scan of the expression and creates the actual {@link PVReader}
     * object that can be monitored through listeners.
     * @param rate rate in Hz; should be between 0 and 50
     * @return the PVReader
     */
    public PVReader<T> every(TimeDuration period) {
        //int scanPeriodMs = (int) (period.getNanoSec() / 1000000);
        
        if (period.getNanoSec() < 5000000) {
            throw new IllegalArgumentException("Current implementation limits the rate to >5ms or <200Hz (requested " + (period.getNanoSec() / 1000000) + "ms)");
        }

        checkDataSourceAndThreadSwitch();

        // Create PVReader and connect
        PVReaderImpl<T> pv = new PVReaderImpl<T>(aggregatedPVExpression.getDefaultName());
        DataRecipe dataRecipe = aggregatedPVExpression.getDataRecipe();
        if (exceptionHandler == null) {
            dataRecipe = dataRecipe.withExceptionHandler(new DefaultExceptionHandler(pv, notificationExecutor));
        } else {
            dataRecipe = dataRecipe.withExceptionHandler(exceptionHandler);
        }
        Function<T> aggregatedFunction = aggregatedPVExpression.getFunction();
        Notifier<T> notifier = new Notifier<T>(pv, aggregatedFunction, PVManager.getReadScannerExecutorService(), notificationExecutor, dataRecipe.getExceptionHandler());
        notifier.startScan(period);
        try {
            source.connect(dataRecipe);
        } catch (RuntimeException ex) {
            dataRecipe.getExceptionHandler().handleException(ex);
        }
        PVRecipe recipe = new PVRecipe(dataRecipe, source, notifier);
        notifier.setPvRecipe(recipe);
        return pv;
    }
}
