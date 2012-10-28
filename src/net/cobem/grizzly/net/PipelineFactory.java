package net.cobem.grizzly.net;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.cobem.grizzly.net.codec.CodecDecoder;
import net.cobem.grizzly.net.codec.CodecEncoder;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;


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
