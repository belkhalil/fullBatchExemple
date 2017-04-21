package ma.adria.batch.runner;

import java.util.Date;

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

public class AppWriter {

	public static void main(String[] args) {

			try {
				ApplicationContext applicationContext = new ClassPathXmlApplicationContext("configuration-context.xml");

				JobLauncher jobLauncher = (JobLauncher) applicationContext.getBean("jobLauncher");
				Job job = (Job) applicationContext.getBean("Writer");
					Date today = new Date();
					JobParametersBuilder paramsBuilder = new JobParametersBuilder();
					paramsBuilder.addDate("distinctDate", today);
					paramsBuilder.addString("targetFileName", args[0]);
				JobExecution execution = jobLauncher.run(job, paramsBuilder.toJobParameters());
			} catch (JobExecutionAlreadyRunningException e) {
				e.printStackTrace();
			} catch (JobRestartException e) {
				e.printStackTrace();
			} catch (JobInstanceAlreadyCompleteException e) {
				e.printStackTrace();
			} catch (JobParametersInvalidException e) {
				e.printStackTrace();
			}
	}

}
