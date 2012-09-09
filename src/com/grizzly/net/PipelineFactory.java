package com.grizzly.net;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.grizzly.net.codec.CodecDecoder;
import com.grizzly.net.codec.CodecEncoder;

public class PipelineFactory implements ChannelPipelineFactory 
{
    @Override
	public ChannelPipeline getPipeline() throws Exception 
    {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("decoder", new CodecDecoder());
        pipeline.addLast("encoder", new CodecEncoder());
        pipeline.addLast("handler", new ConnectionHandler());
        pipeline.addLast("pipelineExecutor", new ExecutionHandler
        		(
                new OrderedMemoryAwareThreadPoolExecutor(
                        200,
                        1048576,
                        1073741824,
                        100,
                        TimeUnit.MILLISECONDS,
                        Executors.defaultThreadFactory()
                )
        ));

        return pipeline;
    }
}
