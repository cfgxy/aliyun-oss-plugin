import com.fit2cloud.jenkins.aliyunoss.AliyunOSSClient;
import com.fit2cloud.jenkins.aliyunoss.AliyunOSSPublisher;
import hudson.Functions;
import hudson.model.*;
import hudson.tasks.*;
import org.junit.*;
import org.jvnet.hudson.test.*;


public class OSSTest {
    @Rule public JenkinsRule j = new JenkinsRule();
    @ClassRule public static BuildWatcher bw = new BuildWatcher();

    @Test
    public void test() throws Exception {
        final String command = "mkdir release; touch release/platform.phar";

        AliyunOSSPublisher publisher = new AliyunOSSPublisher("xxx", "release/*.jar", "dev/xxx");
        publisher.getDescriptor().setAliyunAccessKey("xxx");
        publisher.getDescriptor().setAliyunSecretKey("xxx");
        publisher.getDescriptor().setAliyunEndPointSuffix("oss-cn-hangzhou.aliyuncs.com");

        // Create a new freestyle project with a unique name, with an "Execute shell" build step;
        // if running on Windows, this will be an "Execute Windows batch command" build step
        FreeStyleProject project = j.createFreeStyleProject();
        Builder step = Functions.isWindows() ? new BatchFile(command) : new Shell(command);
        project.getBuildersList().add(step);
        project.getPublishersList().add(publisher);

        // Enqueue a build of the project, wait for it to complete, and assert success
        FreeStyleBuild build = j.buildAndAssertSuccess(project);

        // Assert that the console log contains the output we expect
        j.assertLogContains(command, build);
    }


}
