package core;

/**
 * @author Aneureka
 * @createdAt 2019-12-17 16:51
 * @description
 **/
public interface ChannelPipelineFactory {
    /**
     * Create a channel pipeline instance.
     *
     * @return
     */
    ChannelPipeline getPipeline();
}
