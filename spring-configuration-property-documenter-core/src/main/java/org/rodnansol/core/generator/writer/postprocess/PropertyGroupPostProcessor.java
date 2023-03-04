package org.rodnansol.core.generator.writer.postprocess;

/**
 * Interface to be implemented if a new post processor logic must be added to the flow.
 *
 * @author nandorholozsnyak
 * @since 0.4.0
 */
interface PropertyGroupPostProcessor {

    /**
     * Post processes the incoming command.
     *
     * @param command command and its content to be post processed.
     */
    void postProcess(PostProcessPropertyGroupsCommand command);

}
