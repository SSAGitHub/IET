package com.rsicms.projectshelper.publish.storage.datatype;

import com.rsicms.projectshelper.datatype.RSuiteLmd;

public enum OutputLmd implements RSuiteLmd {

    PUBLICATION_WITH_WARNINGS("publication_with_warnings"),
    PUBLICATION_WITH_ERRORS("publication_with_errors"),
    IS_REMOVABLE_PUBLICATION_RESULT("is_removable_publication_result"),
    USER_CREATOR("user_creator"),
    DATE_CREATED("date_created"),
    PUBLISH_WORKFLOW_LOG("publish_workflow_log"),
    HAS_OUTPUT("has_output"),
    SOURCE_MO_ID("sourceMoId");

    private String lmdName;

    private OutputLmd(String lmdName) {
        this.lmdName = lmdName;
    }

    @Override
    public String getLmdName() {
        return lmdName;
    }

}
