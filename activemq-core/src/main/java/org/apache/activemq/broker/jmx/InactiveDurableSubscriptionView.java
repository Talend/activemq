/**
 * 
 * Copyright 2005-2006 The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.activemq.broker.jmx;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;
import org.apache.activemq.command.SubscriptionInfo;
/**
 * @version $Revision: 1.5 $
 */
public class InactiveDurableSubscriptionView extends SubscriptionView implements  DurableSubscriptionViewMBean {
    
    protected SubscriptionInfo info;
    public InactiveDurableSubscriptionView(String clientId,SubscriptionInfo sub){
        super(clientId,null);
        this.info = sub;
    }
    
    

    
    /**
     * @return the id of the Subscription
     */
    public long getSubcriptionId(){
        return -1;
    }

    /**
     * @return the destination name
     */
    public String getDestinationName(){
        return info.getDestination().getPhysicalName();
       
    }

    /**
     * @return true if the destination is a Queue
     */
    public boolean isDestinationQueue(){
        return false;
    }

    /**
     * @return true of the destination is a Topic
     */
    public boolean isDestinationTopic(){
        return true;
    }

    /**
     * @return true if the destination is temporary
     */
    public boolean isDestinationTemporary(){
        return false;
    }
    /**
     * @return name of the durable consumer
     */
    public String getSubscriptionName(){
        return info.getSubcriptionName();
    }
    
    /**
     * @return true if the subscriber is active
     */
    public boolean isActive(){
        return false;
    }

    /**
     * Browse messages for this durable subscriber
     * 
     * @return messages
     * @throws OpenDataException
     */
    public CompositeData[] browse() throws OpenDataException{
        return null;
    }

    /**
     * Browse messages for this durable subscriber
     * 
     * @return messages
     * @throws OpenDataException
     */
    public TabularData browseAsTable() throws OpenDataException{
        return null;
    }
}