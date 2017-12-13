
package vn.huykn.decode.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Resources {

    @SerializedName("public")
    @Expose
    private List<Public> _public = new ArrayList<Public>();

    /**
     * 
     * @return
     *     The _public
     */
    public List<Public> getPublic() {
        return _public;
    }

    /**
     * 
     * @param _public
     *     The public
     */
    public void setPublic(List<Public> _public) {
        this._public = _public;
    }

}
