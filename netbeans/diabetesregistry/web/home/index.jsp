<%-- 
    Document   : home
    Created on : Oct 30, 2015, 4:11:24 PM
    Author     : Bryan Daniel
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/homeheader.jsp" />
<h2>Home</h2>
<section class="pagecontent">    
    <table class="hometable">
        <tr>
            <td>
                <form action="/diabetesregistry/newpatient">
                    <input class="homepagebutton" type="submit" value="New Patient">
                </form>
            </td>
            <td>
                <form action="/diabetesregistry/updatepatient">
                    <input class="homepagebutton" type="submit" value="Update Patient">
                </form>
            </td>
            <td>
                <form action="/diabetesregistry/dataentry">
                    <input class="homepagebutton" type="submit" value="Data Entry">
                </form>
            </td>
            <td>
                <form action="/diabetesregistry/history">
                    <input class="homepagebutton" type="submit" value="Patient History">
                </form>
            </td>
        </tr>
        <tr>
            <td>
                <form action="/diabetesregistry/treatment">
                    <input class="homepagebutton" type="submit" value="Treatment Entry">
                </form>
            </td>
            <td>
                <form action="/diabetesregistry/quality">
                    <input class="homepagebutton" type="submit" value="Quality Checklist">
                </form>
            </td>
            <td>
                <form action="/diabetesregistry/statistics">
                    <input class="homepagebutton" type="submit" value="Clinic Statistics">
                </form>
            </td>
            <td>
                <form action="/diabetesregistry/calllists">
                    <input class="homepagebutton" type="submit" value="Call Lists">
                </form>
            </td>                
        </tr>
        <tr>                
            <td>
                <form action="/diabetesregistry/progress">
                    <input class="homepagebutton" type="submit" value="Progress Note">
                </form>
            </td>
            <td></td><td></td><td></td>
        </tr>
    </table>
</section>
<jsp:include page="/includes/footer.jsp" />
