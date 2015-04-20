package com.syncapse.jenkinsci.plugins.awscloudformationwrapper;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.AbstractProject;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;

import java.io.IOException;

import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * 
 * 
 * @author erickdovale
 * 
 */
public class SimpleStackBean extends AbstractDescribableImpl<SimpleStackBean> {

	/**
	 * The name of the stack.
	 */
	private String stackName;
	
	private Boolean useIamRole;

	/**
	 * The access key to call Amazon's APIs
	 */
	private String awsAccessKey;

	/**
	 * The secret key to call Amazon's APIs
	 */
	private String awsSecretKey;

	/**
	 * The AWS Region to work against.
	 */
	private Region awsRegion;
        
        private Boolean isPrefixSelected;
        
       
 
	@DataBoundConstructor
	public SimpleStackBean(String stackName, String awsAccessKey,
			String awsSecretKey, Region awsRegion,Boolean isPrefixSelected) {
		this.stackName = stackName;
		this.awsAccessKey = awsAccessKey;
		this.awsSecretKey = awsSecretKey;
		this.awsRegion = awsRegion != null ? awsRegion : Region.getDefault();
                this.isPrefixSelected=isPrefixSelected;
                
          
	}
	
	public Boolean getUseIamRole() {
		return useIamRole;
	}

	public String getStackName() {
		return stackName;
	}

	public String getAwsAccessKey() {
		return awsAccessKey;
	}

	public String getAwsSecretKey() {
		return awsSecretKey;
	}
       public Boolean getIsPrefixSelected() {
		return isPrefixSelected;
	}

	public String getParsedAwsAccessKey(EnvVars env) {
		return env.expand(getAwsAccessKey());
	}

	public String getParsedAwsSecretKey(EnvVars env) {
		return env.expand(getAwsSecretKey());
	}

	public Region getAwsRegion() {
		return awsRegion;
	}


	@Extension
	public static final class DescriptorImpl extends
			Descriptor<SimpleStackBean> {

		@Override
		public String getDisplayName() {
			return "Cloud Formation";
		}

		public FormValidation doCheckStackName(
				@AncestorInPath AbstractProject<?, ?> project,
				@QueryParameter String value) throws IOException {
			if (0 == value.length()) {
				return FormValidation.error("Empty stack name");
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckAwsAccessKey(
				@AncestorInPath AbstractProject<?, ?> project,
				@QueryParameter String value, @QueryParameter Boolean useIamRole) throws IOException {
			if (0 == value.length() && !useIamRole) {
				return FormValidation.error("Empty aws access key");
			} else if (value.length() >0 && useIamRole) {
				return FormValidation.warning("aws access key is not needed for IAM role");
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckAwsSecretKey(
				@AncestorInPath AbstractProject<?, ?> project,
				@QueryParameter String value, @QueryParameter Boolean useIamRole) throws IOException {
			if (0 == value.length() && !useIamRole) {
				return FormValidation.error("Empty aws secret key");
			} else if (value.length() >0 && useIamRole) {
				return FormValidation.warning("aws secret key is not needed for IAM role");
			}
			return FormValidation.ok();
		}
		
		/*protected FormValidation doTestConnection( URL ec2endpoint,
                boolean useInstanceProfileForCredentials, String accessId, String secretKey, String privateKey) throws IOException, ServletException {
               try {
                AWSCredentialsProvider credentialsProvider = createCredentialsProvider(useInstanceProfileForCredentials, accessId, secretKey);
                AmazonEC2 ec2 = connect(credentialsProvider, ec2endpoint);
                ec2.describeInstances();

                if(privateKey==null)
                    return FormValidation.error("Private key is not specified. Click 'Generate Key' to generate one.");

                if(privateKey.trim().length()>0) {
                    // check if this key exists
                    EC2PrivateKey pk = new EC2PrivateKey(privateKey);
                    if(pk.find(ec2)==null)
                        return FormValidation.error("The EC2 key pair private key isn't registered to this EC2 region (fingerprint is "+pk.getFingerprint()+")");
                }

                return FormValidation.ok(Messages.EC2Cloud_Success());
            } catch (AmazonClientException e) {
                LOGGER.log(Level.WARNING, "Failed to check EC2 credential",e);
                return FormValidation.error(e.getMessage());
            }
        }*/

		public ListBoxModel doFillAwsRegionItems() {
			ListBoxModel items = new ListBoxModel();
			for (Region region : Region.values()) {
				items.add(region.readableName, region.name());
			}
			return items;
		}

	}

}