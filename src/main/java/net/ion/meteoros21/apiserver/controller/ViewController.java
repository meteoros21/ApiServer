package net.ion.meteoros21.apiserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;

@Controller
public class ViewController
{
    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login()
    {
        return "login";
    }

    @RequestMapping(value = "/post/list")
    public String postList()
    {
        return "post-list";
    }

    @RequestMapping(value = "/post/detail", method = RequestMethod.GET)
    public String postDetail(Model model, @RequestParam(name="postId") int postId)
    {
        model.addAttribute("postId", postId);
        return "post-detail";
    }

    @RequestMapping(value = "/post/edit", method = RequestMethod.GET)
    public String postEdit(Model model, @RequestParam(name="postId", defaultValue = "0") int postId)
    {
        model.addAttribute("postId", postId);
        return "post-edit";
    }

    @RequestMapping(value = "/post/upload-image", method = RequestMethod.POST)
    public void uploadImage(@RequestParam("CKEditorFuncNum") String funcNum,
                            @RequestParam("upload")MultipartFile file,
                              HttpServletResponse response) throws Exception
    {
        String fileName = saveImageFile("/uploaded-image", file);
        String fileUrl = "http://localhost:8080/uploaded-image/" + fileName;

        String result2 = "<script type='text/javascript'>";
        result2 += "window.parent.CKEDITOR.tools.callFunction('" + funcNum + "', '" + fileUrl + "', '업로드가 완료되었습니다.')</script>";

        sendResponse(response, result2);
    }

    // 4.9.x 이후 버전
    @RequestMapping(value = "/post/upload-image2", method = RequestMethod.POST)
    public void uploadImage2(@RequestParam("upload")MultipartFile file,
                            HttpServletResponse response) throws Exception
    {
        String fileName = saveImageFile("/uploaded-image", file);
        String fileUrl = "http://localhost:8080/uploaded-image/" + fileName;

        HashMap<String, Object> result = new HashMap<>();

        if (fileName != null)
        {
            result.put("uploaded", 1);
            result.put("fileName", fileName);
            result.put("url", fileUrl);
        }
        else
        {
            result.put("uploaded", 0);
        }

        ObjectMapper mapper = new ObjectMapper();
        String test = mapper.writeValueAsString(result);
        sendResponse(response, test);
    }

    private void sendResponse(HttpServletResponse response, String text) throws Exception
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(text);
        writer.close();
    }

    public String saveImageFile(String rootPath, MultipartFile imageFile)
    {
        try
        {
            // 디렉터리를 생성한다.
            String fileDir = servletContext.getRealPath(rootPath);
            File file = new File(fileDir);
            file.mkdirs();

            // 파일 이름과 파일 확장자를 얻는다.
            String fileExt = "";
            String fileNameOnly = "";
            String orgFileName = imageFile.getOriginalFilename();
            int pos = orgFileName.lastIndexOf(".");

            if (pos > 0)
            {
                fileExt = orgFileName.substring(pos + 1).toLowerCase();
                fileNameOnly = orgFileName.substring(0, pos);
            }

            String checkedFileName = orgFileName;

            // 동일한 이름이 있는지 확인한다.
            File fileTest = new File(fileDir + "/" + orgFileName);
            if (fileTest.exists())
            {
                int idx = 1;
                while (true)
                {
                    idx++;
                    String filePath = fileDir + "/" + fileNameOnly + "[" + idx + "]" + "." + fileExt;
                    File test = new File(filePath);

                    if (!test.exists())
                    {
                        checkedFileName = fileNameOnly + "[" + idx + "]" + "." + fileExt;
                        break;
                    }
                }
            }

            File file2 = new File(fileDir + "/" + checkedFileName);
            imageFile.transferTo(file2);

            return checkedFileName;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
