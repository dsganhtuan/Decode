
package vn.huykn.decode.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class DataSource {

    @Expose
    private Resources resources;

    /**
     * 
     * @return
     *     The resources
     */
    public Resources getResources() {
        return resources;
    }

    /**
     * 
     * @param resources
     *     The resources
     */
    public void setResources(Resources resources) {
        this.resources = resources;
    }

}
