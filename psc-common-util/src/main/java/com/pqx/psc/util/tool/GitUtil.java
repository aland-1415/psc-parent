package com.pqx.psc.util.tool;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author quanxing.peng
 * @date 2020/12/14
 */
public class GitUtil {

    public static boolean clone(String username, String pwd, String gitUrl, String branch, String savePath){
        boolean result = false;
        Git git = null;
        try {
            Git.cloneRepository()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, pwd))//设置权限验证
                    .setURI(gitUrl)//设置远程URI
                    .setBranch(branch)//设置clone下来的分支
                    .setDirectory(new File(savePath))//设置下载存放路径
                    .call();
            result = true;
        } catch (GitAPIException e) {
            e.printStackTrace();
//            System.out.println(String.format("gitUrl:%s\n branch:%s\n gitUserName:%s",gitUrl, branch, username));
        } finally {
            if (git != null) {
                git.close();
            }
        }
        return result;
    }

    //pull拉取远程仓库文件
    public static boolean checkoutAndPull(String username, String pwd, String gitpath, String branch){
        boolean result = false;
        //git仓库地址
        Git git = null;
        try {
            git = initGitFromSource(gitpath);
            boolean hasBranch = git.branchList().call()
                                        .stream()
                                        .map(b -> b.getName().replace("refs/heads/", ""))
                                        .filter(b -> b.equals(branch))
                                        .findFirst()
                                        .isPresent();
            git.checkout().setName(branch).setCreateBranch(!hasBranch).call();
            git.pull().setRemoteBranchName(branch)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username,pwd))
                    .call();
            result = true;
        } catch (GitAPIException e) {
            e.printStackTrace();
        }finally {
            if (git != null) {
                git.close();
            }
        }
        return result;
    }

    //add所有更新,还有问题。。
    public static boolean add(String gitpath){
        boolean result = false;
        //git仓库地址
        Git git = null;
        try {
            git = new Git(new FileRepository(gitpath));
            git.add().addFilepattern(gitpath).call();
            result = true;
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }finally {
            if (git != null) {
                git.close();
            }
        }
        return result;
    }

    public static Git initGitFromSource(String gitpath){
        if (!gitpath.contains(".git"))
            gitpath += "/.git";
        File gitfile = new File(gitpath);
        if (!gitfile.exists())
            throw new RuntimeException("当前文件夹下未找到git源文件, gitpath: " + gitpath);
        try {
            return new Git(new FileRepository(gitpath));
        } catch (IOException e) {
            throw new RuntimeException("初始化git失败，gitpath: " + gitpath);
        }
    }

    public static List<String> getRemoteBranchs(String url, String username, String password){
        try {
            Collection<Ref> refList;
            if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
                UsernamePasswordCredentialsProvider pro = new UsernamePasswordCredentialsProvider(username, password);
                refList = Git.lsRemoteRepository().setRemote(url).setCredentialsProvider(pro).call();
            } else {
                refList = Git.lsRemoteRepository().setRemote(url).call();
            }
            return refList.stream().map(r -> r.getName().replace("refs/heads/", "")).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
