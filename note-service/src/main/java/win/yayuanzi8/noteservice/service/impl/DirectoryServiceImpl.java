package win.yayuanzi8.noteservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.noteservice.domain.BaseFile;
import win.yayuanzi8.noteservice.domain.Directory;
import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.domain.Rubbish;
import win.yayuanzi8.noteservice.model.request.NewDirRequest;
import win.yayuanzi8.noteservice.model.response.GetUserDirListResponse;
import win.yayuanzi8.noteservice.repository.DirectoryRepository;
import win.yayuanzi8.noteservice.repository.NoteRepository;
import win.yayuanzi8.noteservice.repository.RubbishRepository;
import win.yayuanzi8.noteservice.service.DirectoryService;
import win.yayuanzi8.noteservice.util.PathUtil;
import win.yayuanzi8.noteservice.util.StatusCode;

import java.util.*;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final NoteRepository noteRepository;
    private final RubbishRepository rubbishRepository;
    private final PathUtil pathUtil;


    @Autowired
    public DirectoryServiceImpl(DirectoryRepository directoryRepository, NoteRepository noteRepository, RubbishRepository rubbishRepository, PathUtil pathUtil) {
        this.directoryRepository = directoryRepository;
        this.noteRepository = noteRepository;
        this.rubbishRepository = rubbishRepository;
        this.pathUtil = pathUtil;
    }

    @Override
    public GetUserDirListResponse findUserDirNameList(Integer uid, String did) {
        Set<BaseFile> fileSet = new TreeSet<>();
        Set<String> regexSet = new HashSet<>();
        Directory directory = directoryRepository.findByUidAndDid(uid, did);
        String parentPath = pathUtil.getDirPath(directory, "");
        regexSet.add("^" + parentPath + pathUtil.getPathSeperator() + pathUtil.getPathRegex() + "$");
        List<Directory> directoryList = directoryRepository.batchQuery(uid, regexSet);
        GetUserDirListResponse response = new GetUserDirListResponse();
        List<Note> noteList = noteRepository.batchQuery(uid, regexSet);
        fileSet.addAll(directoryList);
        fileSet.addAll(noteList);
        response.setFileSet(fileSet);
        return response;
    }

    /**
     * 具体实现：创建Directory对象，设置dirName、createTime、uid、type、parent、path，
     * 然后保存到数据库。
     *
     * @param request 添加文件夹的请求，具体传入上述一大堆参数
     * @param uid     用户uid
     * @return Directory 已保存的文件夹
     */
    @Override
    public Directory createNewDir(NewDirRequest request, Integer uid) {
        Directory parentDir = directoryRepository.findByUidAndDid(uid, request.getParent());
        String parentPath = pathUtil.getDirPath(parentDir, "");
        boolean exist = pathUtil.existsDirInThisLevel(parentPath, uid, request.getDir());
        if (exist) {
            Shift.fatal(StatusCode.DUPLICATE_DIR_NAME);
        }
        Directory directory = new Directory();
        directory.setDirName(request.getDir());
        directory.setCreateTime(new Date());
        directory.setUid(uid);
        directory.setType("directory");
        directory.setPath(parentPath + pathUtil.getPathSeperator() + request.getDir());
        return directoryRepository.save(directory);
    }

    /**
     * 具体实现：首先使用传入的did集合和nid集合查出所有对象，获得所有path，使用MongoDB正则表达式删除目录及子目录、子笔记
     *
     * @param dirIds
     * @param noteIds
     * @param uid
     * @return
     */
    @Override
    public Integer batchDeleteDirAndNote(List<String> dirIds, List<String> noteIds, Integer uid) {
        //删除直接选中的笔记
        int deleted = 0;
        if (noteIds != null && noteIds.size() > 0) {
            deleted += noteRepository.batchDeleteByNid(uid, new HashSet<>(noteIds));
        }
        if (dirIds != null && dirIds.size() > 0) {
            List<Directory> willBeDeletedDirs = directoryRepository.findAllByUidAndDidIn(uid,
                    new HashSet<>(dirIds));
            if (willBeDeletedDirs != null && willBeDeletedDirs.size() > 0) {
                Set<String> pathRegexSet = new HashSet<>();
                willBeDeletedDirs.forEach(dir -> {
                    pathRegexSet.add("^" + dir.getPath());
                });
                deleted += directoryRepository.batchDeleteByPath(pathRegexSet, uid);
                deleted += noteRepository.batchDeleteByPath(pathRegexSet, uid);
            }
        }
        return deleted;
    }

    @Override
    public List<Directory> findUserDirList(Integer uid, String did) {
        Directory parentDir = directoryRepository.findByUidAndDid(uid, did);
        String parentPath = pathUtil.getDirPath(parentDir, "");
        Set<String> regexSet = new HashSet<>();
        regexSet.add("^" + parentPath + pathUtil.getPathSeperator() + pathUtil.getPathRegex() + "$");
        return directoryRepository.batchQuery(uid, regexSet);
    }

    @Override
    public Integer copyDirAndNote(List<String> dirs, List<String> notes, String dest, Integer uid) {
        Integer changeNum = 0;
        Directory destDir = directoryRepository.findByUidAndDid(uid, dest);
        String destPath = pathUtil.getDirPath(destDir, "");
        if (dirs != null && dirs.size() > 0) {
            List<Directory> directoryList = directoryRepository.findAllByUidAndDidIn(uid, new HashSet<>(dirs));
            Set<String> regexSet = new HashSet<>();
            regexSet.add("^" + destPath + pathUtil.getPathSeperator() + pathUtil.getPathRegex() + "$");
            List<Directory> destDirList = directoryRepository.batchQuery(uid, regexSet);
            pathUtil.checkDuplicateDirInOneDir(directoryList, destDirList);
            regexSet = new HashSet<>();
            for (Directory directory : directoryList) {
                regexSet.add("^" + directory.getPath());
            }
            List<Directory> dirWillBeInsert = directoryRepository.batchQuery(uid, regexSet);
            List<Note> noteInDir = noteRepository.batchQuery(uid, regexSet);
            int index = directoryList.get(0).getPath().lastIndexOf(directoryList.get(0).getDirName());
            String parentPath = directoryList.get(0).getPath().substring(0, index - 1);
            if ("".equals(parentPath)) {
                for (Directory directory : dirWillBeInsert) {
                    directory.setDid(null);
                    directory.setCreateTime(new Date());
                    directory.setPath(destPath + directory.getPath());
                }
                for (Note note : noteInDir) {
                    note.setNid(null);
                    note.setCreateTime(new Date());
                    note.setPath(destPath + note.getPath());
                }
            } else {
                for (Directory directory : dirWillBeInsert) {
                    directory.setDid(null);
                    directory.setCreateTime(new Date());
                    directory.setPath(directory.getPath().replaceFirst(parentPath, destPath));
                }
                for (Note note : noteInDir) {
                    note.setNid(null);
                    note.setCreateTime(new Date());
                    note.setPath(note.getPath().replaceFirst(parentPath, destPath));
                }
            }
            directoryRepository.insert(dirWillBeInsert);
            noteRepository.insert(noteInDir);
        }
        if (notes != null && notes.size() > 0) {
            List<Note> noteWillBeInsert = noteRepository.findAllByUidAndNidIn(uid, notes);
            Set<String> regexSet = new HashSet<>();
            regexSet.add("^" + destPath + pathUtil.getPathSeperator() + pathUtil.getPathRegex() + "$");
            List<Note> destNoteList = noteRepository.batchQuery(uid, regexSet);
            pathUtil.checkDuplicateNoteInOneDir(noteWillBeInsert, destNoteList);
            int index = noteWillBeInsert.get(0).getPath().lastIndexOf(noteWillBeInsert.get(0).getTitle());
            String parentPath = noteWillBeInsert.get(0).getPath().substring(0, index - 1);
            if (!"".equals(parentPath)) {
                noteWillBeInsert.forEach(note -> {
                    note.setNid(null);
                    note.setCreateTime(new Date());
                    note.setPath(note.getPath().replaceFirst(parentPath, destPath));
                });
            } else {
                noteWillBeInsert.forEach(note -> {
                    note.setNid(null);
                    note.setCreateTime(new Date());
                    note.setPath(destPath + note.getPath());
                });
            }
            changeNum += noteRepository.insert(noteWillBeInsert).size();
        }
        return changeNum;
    }

    @Override
    public Integer moveToRubbish(List<String> dirs, List<String> notes, Integer uid) {
        Integer changeNum = 0;
        if (dirs != null && dirs.size() > 0) {
            List<Directory> directoryList = directoryRepository.findAllByUidAndDidIn(uid, new HashSet<>(dirs));
            final Set<String> regexSet = new HashSet<>();
            directoryList.forEach(directory -> regexSet.add(directory.getPath()));
            List<Directory> directoriesToRubbish = directoryRepository.batchQuery(uid, regexSet);
            List<Note> noteListToRubbish = noteRepository.batchQuery(uid, regexSet);

            List<Rubbish> rubbishes = new LinkedList<>();

            for (Directory directory : directoriesToRubbish) {
                Rubbish rubbish = new Rubbish();
                rubbish.setFile(directory);
                rubbish.setUid(uid);
                if (dirs.contains(directory.getDid())) {
                    rubbish.setRoot(true);
                }
                rubbishes.add(rubbish);
            }
            for (Note note : noteListToRubbish) {
                Rubbish rubbish = new Rubbish();
                rubbish.setUid(uid);
                rubbish.setFile(note);
                rubbishes.add(rubbish);
            }
            rubbishRepository.insert(rubbishes);

            changeNum += directoryRepository.batchDeleteByPath(regexSet, uid);
            changeNum += noteRepository.batchDeleteByPath(regexSet, uid);

        }
        if (notes != null && notes.size() > 0) {
            List<Note> noteToRubbish = noteRepository.findAllByUidAndNidIn(uid, notes);
            List<Rubbish> rubbishes = new LinkedList<>();
            for (Note note : noteToRubbish) {
                Rubbish rubbish = new Rubbish();
                rubbish.setFile(note);
                rubbish.setUid(uid);
                rubbish.setRoot(true);
                rubbishes.add(rubbish);
            }
            rubbishRepository.insert(rubbishes);
            changeNum += noteRepository.batchDeleteByNid(uid, new HashSet<>(notes));
        }
        return changeNum;
    }

    @Override
    public Integer rename(String did, String newName, Integer uid) {
        Directory directory = directoryRepository.findByUidAndDid(uid, did);
        if (directory == null) {
            Shift.fatal(StatusCode.DIR_NOT_EXIST);
        }
        String oldName = directory.getDirName();
        if (oldName.equals(newName)) {
            Shift.fatal(StatusCode.SAME_AS_OLD_DIR_NAME);
        }
        String oldPath = directory.getPath();
        int index = oldPath.lastIndexOf(directory.getDirName());
        String parentPath = oldPath.substring(0, index - 1);
        if (pathUtil.existsDirInThisLevel(parentPath, uid, newName)) {
            Shift.fatal(StatusCode.DUPLICATE_DIR_NAME);
        }
        Set<String> regexSet = new HashSet<>();
        regexSet.add("^" + directory.getPath() + pathUtil.getPathSeperator() + pathUtil.getPathRegex());
        List<Directory> directoryList = directoryRepository.batchQuery(uid, regexSet);
        List<Note> noteList = noteRepository.batchQuery(uid, regexSet);
        String newPath = parentPath + newName;
        directory.setDirName(newName);
        directory.setPath(newPath);
        for (Directory d : directoryList) {
            d.setPath(d.getPath().replaceFirst(oldPath, newPath));
        }
        for (Note n : noteList) {
            n.setPath(n.getPath().replaceFirst(oldPath, newPath));
        }
        Integer changeNum = 0;
        changeNum += directoryRepository.batchUpdate(uid, directoryList);
        changeNum += noteRepository.batchUpdate(uid, noteList);
        return changeNum;
    }

    @Override
    public Integer moveDirAndNote(List<String> dirs, List<String> notes, String dest, Integer uid) {
        Integer changeNum = 0;
        Directory parentDir = directoryRepository.findByUidAndDid(uid, dest);
        String destPath = pathUtil.getDirPath(parentDir, "");
        if (dirs != null && dirs.size() > 0) {
            Set<String> checkedDirIds = new HashSet<>(dirs);
            List<Directory> checkDirs = directoryRepository.findAllByUidAndDidIn(uid, checkedDirIds);
            Set<String> regexSet = new HashSet<>();
            regexSet.add("^" + destPath + pathUtil.getPathSeperator() + pathUtil.getPathRegex() + "$");
            List<Directory> destDirList = directoryRepository.batchQuery(uid, regexSet);
            pathUtil.checkDuplicateDirInOneDir(checkDirs, destDirList);
            int index = checkDirs.get(0).getPath().lastIndexOf(checkDirs.get(0).getDirName());
            String parentPath = checkDirs.get(0).getPath().substring(0, index - 1);
            changeNum += moveDirectory(checkDirs, checkedDirIds, dest, destPath, uid, parentPath);
            changeNum += moveNoteInDir(checkDirs, destPath, uid, parentPath);
        }
        if (notes != null && notes.size() > 0) {
            changeNum += moveNote(notes, dest, destPath, uid);
        }
        return changeNum;
    }

    private Integer moveNote(List<String> notes, String dest, String destPath, Integer uid) {
        List<Note> notesList = noteRepository.findAllByUidAndNidIn(uid, notes);
        Set<String> regexSet = new HashSet<>();
        regexSet.add("^" + destPath + pathUtil.getPathSeperator() + pathUtil.getPathRegex() + "$");
        List<Note> destNoteList = noteRepository.batchQuery(uid, regexSet);
        pathUtil.checkDuplicateNoteInOneDir(notesList, destNoteList);
        int index = notesList.get(0).getPath().lastIndexOf(notesList.get(0).getTitle());
        String parentPath = notesList.get(0).getPath().substring(0, index - 1);
        if ("".equals(parentPath)) {
            for (Note note : notesList) {
                note.setPath(destPath + note.getPath());
            }
        } else {
            for (Note note : notesList) {
                String newPath = note.getPath().replaceFirst(parentPath, destPath);
                note.setPath(newPath);
            }
        }
        return noteRepository.batchUpdate(uid, notesList);
    }

    private Integer moveNoteInDir(List<Directory> checkDirs,
                                  String destPath,
                                  Integer uid,
                                  String parentPath) {
        Set<String> regexSet = new HashSet<>();
        for (Directory checkDir : checkDirs) {
            regexSet.add("^" + checkDir.getPath());
        }
        List<Note> noteWillBeUpdated = noteRepository.batchQuery(uid, regexSet);
        if ("".equals(parentPath)) {
            for (Note note : noteWillBeUpdated) {
                note.setPath(destPath + note.getPath());
            }
        } else {
            for (Note note : noteWillBeUpdated) {
                String tempPath = note.getPath();
                //替换
                tempPath = tempPath.replaceFirst(parentPath, destPath);
                note.setPath(tempPath);
            }
        }
        //批量更新
        return noteRepository.batchUpdate(uid, noteWillBeUpdated);
    }

    private Integer moveDirectory(List<Directory> checkDirs,
                                  Set<String> checkedDirIds,
                                  String dest,
                                  String destPath,
                                  Integer uid, String parentPath) {
        //查询被移动目录的所有子目录，使用正则表达式查询
        Set<String> regexSet = new HashSet<>();
        for (Directory checkDir : checkDirs) {
            regexSet.add("^" + checkDir.getPath());
        }
        List<Directory> dirWillBeUpdated = directoryRepository.batchQuery(uid, regexSet);
        if ("".equals(parentPath)) {
            for (Directory directory : dirWillBeUpdated) {
                directory.setPath(destPath + directory.getPath());
            }
        } else {
            for (Directory dir : dirWillBeUpdated) {
                String tempPath = dir.getPath();
                //替换本目录及子目录路径
                tempPath = tempPath.replaceFirst(parentPath, destPath);
                dir.setPath(tempPath);
            }
        }
        //批量更新
        return directoryRepository.batchUpdate(uid, dirWillBeUpdated);
    }

    private boolean checkDestPath(List<Directory> checkDirs, String destPath) {
        /*
        例如：,root,test,移动到,root,，那么此时可以移动，因为移动目的地不在本目录或子目录中，
        反之，,root,test,移动到,root,test,test2,，就不行，因为
        */
        for (Directory checkDir : checkDirs) {
            if (destPath.contains(checkDir.getPath())) {
                return false;
            }
        }
        return true;
    }


}
