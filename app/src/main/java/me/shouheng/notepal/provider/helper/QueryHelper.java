package me.shouheng.notepal.provider.helper;

import android.content.Context;

import java.util.List;

import me.shouheng.notepal.model.MindSnagging;
import me.shouheng.notepal.model.Note;
import me.shouheng.notepal.model.enums.Status;
import me.shouheng.notepal.provider.MindSnaggingStore;
import me.shouheng.notepal.provider.NotesStore;
import me.shouheng.notepal.provider.schema.BaseSchema;
import me.shouheng.notepal.provider.schema.MindSnaggingSchema;
import me.shouheng.notepal.provider.schema.NoteSchema;
import me.shouheng.notepal.util.tools.SearchConditions;


/**
 * Created by WngShhng on 2017/12/11.*/
public class QueryHelper {

    private Context context;

    private SearchConditions conditions;

    public static QueryHelper newInstance(Context context) {
        return new QueryHelper(context);
    }

    private QueryHelper(Context context) {
        this.context = context;
    }

    public List<Note> getNotes(String queryString) {
        return NotesStore.getInstance(context).get((conditions.isIncludeTags() ?
                        " ( " + NoteSchema.TITLE + " LIKE ? " + " OR " + NoteSchema.TAGS + " LIKE ? ) " :
                        NoteSchema.TITLE + " LIKE ? " ) + getQueryConditions(),
                conditions.isIncludeTags() ?
                        new String[]{"%" + queryString + "%", "%" + queryString + "%"} :
                        new String[]{"%" + queryString + "%"},
                NoteSchema.ADDED_TIME + " DESC ");
    }

    public List<MindSnagging> getMindSnaggings(String queryString) {
        return MindSnaggingStore.getInstance(context).get(MindSnaggingSchema.CONTENT + " LIKE ? " + getQueryConditions(),
                new String[]{"%" + queryString + "%"}, MindSnaggingSchema.ADDED_TIME + " DESC ");
    }

    private String getQueryConditions() {
        StringBuilder sb = new StringBuilder();
        sb.append(conditions.isIncludeArchived() ? "" : " AND " + BaseSchema.STATUS + " != " + Status.ARCHIVED.id);
        sb.append(conditions.isIncludeTrashed() ? "" : " AND " + BaseSchema.STATUS + " != " + Status.TRASHED.id);
        return sb.toString();
    }

    public void setConditions(SearchConditions conditions) {
        this.conditions = conditions;
    }
}