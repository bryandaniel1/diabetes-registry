/* 
 * Copyright 2017 Bryan Daniel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Calculates BMI using weight and height values
 * @returns {undefined}
 */
function calculatebmi()
{
    var weight = parseFloat(document.getElementById("weight").value);
    var feet = parseFloat(document.getElementById("heightFeet").value);
    var inches = parseFloat(document.getElementById("heightInches").value);
    if ((weight) && (feet)) {
        if ((inches.length === 0) || (inches === 0)) {
            inches = 0;
        }
        var totalInches = (feet * 12) + inches;
        var bmi = ((weight * 703) / (totalInches * totalInches)).toFixed(2);
        document.getElementById("bmi").value = bmi;
        if (bmi > 25) {
            var reductionGoal = (weight * 0.07).toFixed(2);
            document.getElementById("weightreductioninput").value
                    = reductionGoal;
        } else {
            document.getElementById("weightreductioninput").value = 0;
        }
    }
}


/**
 * Selects or deselects all checkboxes based on the status of the master checkbox
 * @param {type} master
 * @returns {undefined}
 */
function selectAll(master) {
    checkboxes = document.getElementsByName('emailList');
    for (var i = 0, n = checkboxes.length; i < n; i++) {
        if (!checkboxes[i].disabled) {
            checkboxes[i].checked = master.checked;
        }
    }
}

/**
 * Adds styling available in jqueryui to buttons, tables, inputs, and sections
 * Sets the class for any tables containing child elements of class 'outoftarget'
 * Sets the class for any tables containing child elements of class 'missing'
 * Sets the datepickers for the date inputs
 * Toggles the display of new checklist forms and of edit user forms
 */
$(document).ready(function () {
    $('.button').button();
    $('.homepagebutton').button();
    $('section').addClass("ui-corner-all");
    $('.historytable').addClass("ui-corner-all");
    $('table').not(".minitable, .historytable, #historyselecttable")
            .addClass("ui-widget ui-widget-content ui-corner-all");
    $('.minitable').addClass("ui-corner-all");
    $('input:text').addClass("ui-widget ui-widget-content ui-corner-all");
    $(".outoftarget")
            .parents("table")
            .addClass("outoftarget");
    $(".missing")
            .parents("table")
            .addClass("missing");
    $(".datepicker").not("#from, #to").datepicker();
    $(".datepicker").not("#from, #to").datepicker("option", "dateFormat", "yy-mm-dd");
    $(".datepicker").not("#from, #to").datepicker("option", "changeYear", "true");
    $(".datepicker").not("#from, #to").datepicker("option", "yearRange", "-100:+0");
    $("#from").datepicker({
        numberOfMonths: 2,
        dateFormat: "yy-mm-dd",
        onSelect: function () {
            var minDate = $("#from").datepicker('getDate');
            minDate.setDate(minDate.getDate() + 1);
            $("#to").datepicker("option", "minDate", minDate)
        }
    });
    $("#to").datepicker({
        numberOfMonths: 2,
        dateFormat: "yy-mm-dd",
        onSelect: function () {
            var maxDate = $("#to").datepicker('getDate');
            maxDate.setDate(maxDate.getDate() - 1);
            $("#from").datepicker("option", "maxDate", maxDate)
        }
    });
    $("#newchecklistitembutton").click(function(){
        $("#newchecklistitemform").show();
        $("#newchecklistitembutton").hide();
    });
    $("#cancelnewchecklistitem").click(function(e){
        e.preventDefault();
        $("#newchecklistitembutton").show();
        $("#newchecklistitemform").hide();        
    });
    $("#edituserbutton").click(function(){
        $("#editUserForm").show();
        $("#userTable").hide();
    });
    $("#editusercancel").click(function(e){
        e.preventDefault();
        $("#userTable").show();
        $("#editUserForm").hide();        
    });
});

