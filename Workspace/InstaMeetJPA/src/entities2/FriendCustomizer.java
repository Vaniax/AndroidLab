package entities2;

import javax.persistence.ManyToMany;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.mappings.ManyToManyMapping;


public class FriendCustomizer implements DescriptorCustomizer  {

	@Override
	public void customize(ClassDescriptor descriptor) throws Exception {
		// TODO Auto-generated method stub
		ManyToManyMapping mapping = (ManyToManyMapping)descriptor.getMappingForAttributeName("friendMapping");
		
		ExpressionBuilder eb = new ExpressionBuilder(mapping.getReferenceClass());
		eb.get("friends");
		
	}

}
