/*
 * Created on 16.10.2004
 */
package net.sourceforge.fullsync.schedule;

import java.util.ArrayList;

import org.apache.log4j.Logger;



/**
 * @author <a href="mailto:codewright@gmx.net">Jan Kopcsek</a>
 */
public class SchedulerImpl implements Scheduler, Runnable
{
    private Logger logger = Logger.getLogger( Scheduler.class );
    private ScheduleTaskSource scheduleSource;
	private Thread worker;
	private boolean running;
	private boolean enabled;
	
	private ArrayList schedulerListeners;
	
	public SchedulerImpl()
	{
		this( null );
	}
	public SchedulerImpl( ScheduleTaskSource source )
	{
		scheduleSource = source;
		schedulerListeners = new ArrayList();
	}
	
	public void setSource( ScheduleTaskSource source )
	{
		scheduleSource = source;
	}
	public ScheduleTaskSource getSource()
    {
        return scheduleSource;
    }
	public void addSchedulerChangeListener(SchedulerChangeListener listener) 
	{
    	schedulerListeners.add(listener);
    }
    public void removeSchedulerChangeListener(SchedulerChangeListener listener) 
    {
    	schedulerListeners.remove(listener);
    }
    protected void fireSchedulerChangedEvent() 
    {
    	for (int i = 0; i < schedulerListeners.size(); i++) {
    		((SchedulerChangeListener)schedulerListeners.get(i)).schedulerStatusChanged(enabled);
    	}
    }
	public boolean isRunning()
    {
        return running;
    }
	public boolean isEnabled()
	{
	    return enabled;
	}

	public void start()
	{
	    if( enabled )
	        return;
	    
		enabled = true;
		if( worker == null || !worker.isAlive() )
		{
			worker = new Thread( this, "Scheduler" );
			worker.setDaemon( true );
			worker.start();
		}
		fireSchedulerChangedEvent();
	}
	
	public void stop()
	{
	    if( !enabled || worker == null )
	        return;
	    
		enabled = false;
		if( running )
		{
			worker.interrupt();
		}
		try {
            worker.join();
        } catch( InterruptedException e ) {
        } finally {
            worker = null;
        }
        fireSchedulerChangedEvent();
	}
	
	public void refresh()
    {
	    if( worker != null )
	        worker.interrupt();
    }
	
	public void run()
	{
		running = true;
		while( enabled )
		{
		    long now = System.currentTimeMillis();
		    if( logger.isDebugEnabled() )
		        logger.debug( "searching for next task after "+now );
			ScheduleTask task = scheduleSource.getNextScheduleTask();
			if( logger.isDebugEnabled() )
			    logger.debug( "found: "+task.toString()+" at "+task.getExecutionTime() );
			
			if( task == null )
			{
				// TODO log sth here ?
				break;
			}
			
			long nextTime = task.getExecutionTime();
			try {
				if( logger.isDebugEnabled() )
					logger.debug( "waiting for "+(nextTime-now)+" mseconds" );
				if( nextTime >= now )
					Thread.sleep( nextTime-now );
				if( logger.isDebugEnabled() )
				    logger.debug( "Running task "+task );
				task.run();
			} catch( InterruptedException ie ) {
			}
			
		}
		running = false;
		if( enabled )
		{
		    enabled = false;
		    fireSchedulerChangedEvent();
		}
	}
}