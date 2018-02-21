package win.yayuanzi8.noteservice.service;


import win.yayuanzi8.noteservice.domain.Note;

public interface NoteService {

    Note createNewNote(String title, String parent, Integer uid);

    Note updateNote(String nid, String title, String content, Integer uid);

    Integer rename(String nid, String newName, Integer uid);
}
