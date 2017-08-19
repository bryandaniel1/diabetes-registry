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
$(document).ready(function () {
    $("#terminationConfirmation").hide();
    $("#resetPasswordConfirmation").hide();
    $("#deleteEmailMessageConfirmation").hide();
    $("#terminate").click(function (event) {
        event.preventDefault();
        $("#terminationConfirmation").dialog({
            title: "Confirmation",
            closeOnEscape: true,
            modal: true,
            buttons:
                    [{
                            text: "Yes",
                            click: function () {
                                $(this).dialog("close");
                                $("#terminateForm").submit();
                            }
                        },
                        {
                            text: "No",
                            click: function () {
                                $(this).dialog("close");
                            }
                        }]
        });
        return false;
    });
    $("#reset").click(function (event) {
        event.preventDefault();
        $("#resetPasswordConfirmation").dialog({
            title: "Confirmation",
            closeOnEscape: true,
            modal: true,
            buttons:
                    [{
                            text: "Yes",
                            click: function () {
                                $(this).dialog("close");
                                $("#resetPasswordForm").submit();
                            }
                        },
                        {
                            text: "No",
                            click: function () {
                                $(this).dialog("close");
                            }
                        }]
        });
        return false;
    });
    $("#delete").click(function (event) {
        event.preventDefault();
        $("#deleteEmailMessageConfirmation").dialog({
            title: "Confirmation",
            closeOnEscape: true,
            modal: true,
            buttons:
                    [{
                            text: "Yes",
                            click: function () {
                                $(this).dialog("close");
                                $("#deleteForm").submit();
                            }
                        },
                        {
                            text: "No",
                            click: function () {
                                $(this).dialog("close");
                            }
                        }]
        });
        return false;
    });
})

