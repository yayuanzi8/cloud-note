package win.yayuanzi8.noteservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.domain.ShareNote;
import win.yayuanzi8.noteservice.model.NoteView;
import win.yayuanzi8.noteservice.repository.NoteRepository;
import win.yayuanzi8.noteservice.repository.ShareRepository;
import win.yayuanzi8.noteservice.service.ShareNoteService;
import win.yayuanzi8.noteservice.util.StatusCode;

import java.util.*;

@Service
public class ShareNoteServiceImpl implements ShareNoteService {

    private final ShareRepository shareRepository;
    private final NoteRepository noteRepository;

    @Autowired
    public ShareNoteServiceImpl(ShareRepository shareRepository, NoteRepository noteRepository) {
        this.shareRepository = shareRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    public ShareNote share(String nid,Integer uid) {
        ShareNote sn = shareRepository.findByNid(nid);
        if (sn != null){
            Shift.fatal(StatusCode.HAS_SHARE);
        }
        ShareNote shareNote = new ShareNote();
        shareNote.setNid(nid);
        shareNote.setUid(uid);
        shareNote.setShareDate(new Date());
        return shareRepository.insert(shareNote);
    }

    @Override
    public Set<NoteView> getMyShare(Integer uid) {
        List<ShareNote> shareList = shareRepository.findAllByUidOrderByShareDateDesc(uid);
        final List<String> shareNids = new ArrayList<>(shareList.size());
        final Map<String,ShareNote> shareNoteMap = new HashMap<>();
        shareList.forEach(shareNote -> {
            shareNids.add(shareNote.getNid());
            shareNoteMap.put(shareNote.getNid(),shareNote);
        });
        List<Note> shareNotes = noteRepository.findAllByUidAndNidIn(uid,shareNids);
        Set<NoteView> noteViewSet = new TreeSet<>();
        for (Note shareNote : shareNotes) {
            NoteView noteView = new NoteView();
            ShareNote temp = shareNoteMap.get(shareNote.getNid());
            noteView.setNid(shareNote.getNid());
            noteView.setSid(temp.getSid());
            noteView.setTitle(shareNote.getTitle());
            noteView.setShareDate(temp.getShareDate());
            noteViewSet.add(noteView);
        }
        return noteViewSet;
    }

    @Override
    public Note getShare(String sid) {
        ShareNote sn = shareRepository.findOne(sid);
        if (sn == null){
            Shift.fatal(StatusCode.SHARE_NOT_EXISTS);
        }
        return noteRepository.findOne(sn.getNid());
    }

    @Override
    public Integer cancelShare(String sid, Integer uid) {
        return shareRepository.deleteByUidAndSid(uid,sid);
    }

}
