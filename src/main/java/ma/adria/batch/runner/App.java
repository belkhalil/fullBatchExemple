package ma.adria.batch.runner;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	private static Long CODE_REFERENCE;
	
	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		final long startTime = System.currentTimeMillis();	
		try{

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("configuration-context.xml");
		JobLauncher jobLauncher = (JobLauncher) applicationContext.getBean("jobLauncher");
		
		Job job = (Job) applicationContext.getBean("FluxTaskletJob");
		//first job's parameters
		    CODE_REFERENCE=Long.parseLong(args[0]);
			Date today = new Date();
			JobParametersBuilder paramsBuilder = new JobParametersBuilder();
			
			paramsBuilder.addDate("distinctDate", today);
			paramsBuilder.addLong("CODE_REFERENCE", CODE_REFERENCE);
            //run the first job
			//JobExecution execution = jobLauncher.run(job, paramsBuilder.toJobParameters());
			
			/*
			 * second job is disabled */
			//Second job's parameters
			Job job2 = (Job) applicationContext.getBean("JobWriterInDB");
			JobParametersBuilder paramsBuilder2 = new JobParametersBuilder();
			paramsBuilder2.addDate("distinctDate", today);
			paramsBuilder2.addString("targetFileName", args[1]);
			
			//run the second job
		    JobExecution execution2 = jobLauncher.run(job2, paramsBuilder2.toJobParameters());
		     
			// the third job
			/*Job jobXml = (Job) applicationContext.getBean("jobXml");
			JobParametersBuilder paramsBuilder3 = new JobParametersBuilder();
			
			paramsBuilder3.addDate("distinctDate", today);
			paramsBuilder3.addString("targetFileName", args[1]);
			paramsBuilder3.addString("xmlFile", args[2]);
			JobExecution execution3 = jobLauncher.run(jobXml, paramsBuilder3.toJobParameters());*/
		
			
			//ExitStatus stat = execution.getExitStatus();
			
//			if(stat.getExitCode().equals(ExitStatus.COMPLETED.getExitCode()))
//			{
//				System.out.println("Finished--------------");
//				System.exit(0);
//			}
//			else
//			{
//				List<Throwable> exs = execution.getAllFailureExceptions();
//				for(Throwable c : exs)
//				{
//					if(c instanceof Exception)
//					{
//						LOGGER.error(c.getMessage(), c);
//						System.exit(10);
//					}
//					else
//					{
//						LOGGER.error("unknown Exception");
//						System.exit(99);
//					}
//				}
//			}
				
		} catch (JobExecutionAlreadyRunningException ex) {
			LOGGER.error(ex.getMessage(),ex);
            System.exit(2);
        } catch (JobRestartException ex) {
        	LOGGER.error(ex.getMessage(),ex);
            System.exit(3);
        } catch (JobInstanceAlreadyCompleteException ex) {
        	LOGGER.error(ex.getMessage(),ex);
            System.exit(4);
        } catch (JobParametersInvalidException ex) {
        	LOGGER.error(ex.getMessage(),ex);
            System.exit(5);
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(),ex);
            System.exit(7);
        } finally {
			//final long endTime = System.currentTimeMillis();
//			System.out.println("Finished ---" + "FluxTaskletJob" + " in: "
//					+ calculateDuration(startTime, endTime) + "MILLISECONDS!");
                        
		}
		
		
	}

}