package org.typist.engine;

import org.typist.utils.*;
import org.typist.beans.*;
import org.typist.constants.*;

public class SourceTextFetcher {
    private SourceTextBean stb;
    private RepositoryValidator rv;
    
    public SourceTextFetcher() { 
            this.rv = new RepositoryValidator(Constants.REPOS_PATH);
    }

    public SourceTextBean fetchSourceText() {
        this.stb = rv.validateRepository();
        // Set stb in TextValidator (to be used when validating text)
        TextValidator tv = new TextValidator();
        tv.setSourceTextBean(stb);
        return stb;
    }
}
