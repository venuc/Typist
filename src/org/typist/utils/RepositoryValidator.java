package org.typist.utils;

import java.io.*;

import org.typist.beans.*;
import org.typist.constants.*;
import org.typist.exceptions.*;

public class RepositoryValidator {
    // This checks if the repository file is valid sets appropiate text in
    // SourceTextBean. Note that only the relevant paragraph is checked as
    // required and NOT the entire repository file.
    
    private String fileName;
    private int paraNo;           // para to fetch from repository
    private int maxPara;          // max paragraphs in repository
    private SourceTextBean sourceTextBean = new SourceTextBean();
    private StringBuilder strBuilder = new StringBuilder("");

    public RepositoryValidator(String fileName) {
        this.fileName = fileName;
    }

    public SourceTextBean validateRepository () {
        int paraNoTemp = -1;
        String currentLine = ""; // this points to the current line being
                                 // parsed
        File repository;
        FileReader fr;
        BufferedReader br;
        try {
            // Read repos for max para number
            repository = new File(fileName);
            fr = new FileReader(repository);
            br = new BufferedReader(fr);
            // Read first line
            maxPara = Integer.parseInt(br.readLine());
            // Generate random paragraph number
            paraNo = RandomNumberGenerator.getRandomParaNumber(maxPara);
            // Search for paraNo in repos and build source text
            while ((currentLine = br.readLine()) != null) {
                if ('$' == currentLine.charAt(0)) {
                    paraNoTemp = Integer.parseInt(currentLine.substring(1));
                }
                // build source text if para found else continue; break if 
                // source text is built
                if (paraNoTemp == paraNo) {
                    // Dont add the para counter to the source string
                    if ('$' != currentLine.charAt(0)) {
                        strBuilder.append(currentLine + '\n');
                    }
                } else if ((paraNoTemp != paraNo) 
                        && (strBuilder.length() > 0)) {
                    break;
                } else {
                    continue;
                }
            }
            br.close();
            // Check if source string has been built
            if (strBuilder.length() == 0) {
                throw new SourceTextIsNullException();
            }
            // set text bean with the text read from repos.
            sourceTextBean.setStatus(Constants.SUC_GOOD_STATUS);
            sourceTextBean.setText(strBuilder.toString());
        } catch (SourceTextIsNullException stin) {
            // write status / err to SourceTextBean
            sourceTextBean.setStatus(Constants.ERR_BAD_STATUS);
            sourceTextBean.setText(Constants.ERR_SOURCE_TEXT_IS_NULL);
            return sourceTextBean;
        } catch (NumberFormatException nfe) {
            // wirte status / err to SourceTextBean
//            br.close();
            sourceTextBean.setStatus(Constants.ERR_BAD_STATUS);
            sourceTextBean.setText(Constants.ERR_INVALID_PARA_NO
                    + "Thrown: " + nfe.toString());
            System.out.println(nfe);
            return sourceTextBean;
        } catch (IOException ioe) {
            // write status / err to SourceTextBean
            sourceTextBean.setStatus(Constants.ERR_BAD_STATUS);
            sourceTextBean.setText(Constants.ERR_IO_EXCEPTION
                    + "Thrown: " + ioe.toString());
            System.out.println(ioe);
            return sourceTextBean;
        } catch (Exception e) {
            // write status / err to SourceTextBean
            sourceTextBean.setStatus(Constants.ERR_BAD_STATUS);
            sourceTextBean.setText(Constants.ERR_GENERIC_EXCEPTION
                    + "Thrown: " + e.toString());
            System.out.println(e);
        }
        return sourceTextBean;
    }
}
