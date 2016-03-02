var $answer = $('#answer');
var $typeDropDown = $('#question-type');

$answer.append(renderQuestionResponse());

$typeDropDown.bind('change', function(event) {
    $answer.empty();
    if ($typeDropDown.val() === 'question-response' || $typeDropDown.val() === 'fill-in' || $typeDropDown.val() === 'picture-response') {
        $answer.append(renderQuestionResponse());
    } else if ($typeDropDown.val() === 'multi-choice') {
        $answer.append(renderMultipleChoice());
    } else if ($typeDropDown.val() === 'multi-choice-multi-answer') {
        $answer.append(renderMultiChoiceMultiAnswer());
    } else if ($typeDropDown.val() === 'multi-answer') {
        $answer.append(renderMultiAnswer());
    } else if ($typeDropDown.val() === 'matching') {
        $answer.append(renderMatching());
    }
});

/*
 * Renders answer input for Question-Response,
 * Fill in the blank, and Picture-Response type questions.
 *
 * Usage for handling post requests:
 * String answer1Text = request.getParameter("answer1");
 * String answer2Text = request.getParameter("answer2");
 * String answer3Text = request.getParameter("answer3");
 * 
 * Make sure to check if the answer is null (user left one or more inputs blank). 
 * All non-null answers should be created as answer objects with valid=true.
 *
 * Answer ans1 = new Answer(answer1Text, questionNumber, quiz_id, true);
 */
function renderQuestionResponse() {
    return tag('div', {}, [
        tag('p', {}, 'Provide up to three valid answers (case-insensitive):'),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer1' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer2' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer3' }, [])
    ]);
};

/*
 * Renders answer input for Multiple Choice type questions.
 *
 * Usage for handling post requests:
 * String validAnswer = request.getParameter("answer"); // returns the string containing the valid answer
 *
 * // get all valid and non-valid answers:
 *
 * String answer1Text = request.getParameter("answer1");
 * String answer2Text = request.getParameter("answer2");
 * String answer3Text = request.getParameter("answer3");
 * String answer4Text = request.getParameter("answer4");
 *
 * if (validAnswer.equals(answer1Text)) {
 *		Answer ans1 = new Answer(answer1Text, questionNumber, quiz_id, true);
 * } else {
 *		Answer ans1 = new Answer(answer1Text, questionNumber, quiz_id, false);
 * }
 */
function renderMultipleChoice() {

    var answerInputs = tag('div', {}, [
        tag('p', {}, 'Provide four answers and select the valid answer:'),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer1' }, []),
        tag('input', { 'type': 'radio', 'name': 'answer', 'value': 'blank', 'checked': 'checked' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer2' }, []),
        tag('input', { 'type': 'radio', 'name': 'answer', 'value': 'blank' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer3' }, []),
        tag('input', { 'type': 'radio', 'name': 'answer', 'value': 'blank' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer4' }, []),
        tag('input', { 'type': 'radio', 'name': 'answer', 'value': 'blank' }, []),
        tag('br', {}, [])
    ]);

    // sets value for each radio button to be the string in the text input next to it
    $(answerInputs).children('.answers').each(function() {
        $(this).bind('change', function(event) {
            $(this).next().attr('value', $(this).val());
        });
    });

    return answerInputs;
};

/*
 * Renders answer input for Multiple Choice Multiple Answer type questions.
 *
 * Usage for handling post requests:
 * String validAnswers[] = request.getParameterValues("answer"); // returns an array of strings containing the valid answers
 *
 * // get all valid and non-valid answers:
 *
 * String answer1Text = request.getParameter("answer1");
 * String answer2Text = request.getParameter("answer2");
 * ...
 * String answer5Text = request.getParameter("answer5");
 *
 * if (Arrays.asList(validAnswers).contains(answer1Text)) {
 *		Answer ans1 = new Answer(answer1Text, questionNumber, quiz_id, true);
 * } else {
 *		Answer ans1 = new Answer(answer1Text, questionNumber, quiz_id, false);
 * }
 */
function renderMultiChoiceMultiAnswer() {

    var answerInputs = tag('div', {}, [
        tag('p', {}, 'Provide up to five answers and select at least one valid answer choice:'),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer1' }, []),
        tag('input', { 'type': 'checkbox', 'name': 'answer', 'value': 'blank' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer2' }, []),
        tag('input', { 'type': 'checkbox', 'name': 'answer', 'value': 'blank' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer3' }, []),
        tag('input', { 'type': 'checkbox', 'name': 'answer', 'value': 'blank' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer4' }, []),
        tag('input', { 'type': 'checkbox', 'name': 'answer', 'value': 'blank' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer5' }, []),
        tag('input', { 'type': 'checkbox', 'name': 'answer', 'value': 'blank' }, []),
        tag('br', {}, [])
    ]);

    $(answerInputs).children('.answers').each(function() {
        $(this).bind('change', function(event) {
            $(this).next().attr('value', $(this).val());
        });
    });

    return answerInputs;
}

/*
 * Renders answer input for Mult-Answer type questions.
 *
 * Usage for handling post requests:
 * 
 * if (request.getParameter("ordered") != null) // do some logic requiring ordered answers
 *
 * // get all valid and non-valid answers:
 *
 * String answer1Text = request.getParameter("answer1");
 * String answer2Text = request.getParameter("answer2");
 * ...
 * String answer10Text = request.getParameter("answer10");
 *
 * Make sure to check if the answer is null (user left one or more inputs blank). 
 * All non-null answers should be created as answer objects with valid=true:
 *
 * Answer ans1 = new Answer(answer1Text, questionNumber, quiz_id, true);
 */
function renderMultiAnswer() {

    var answerInputs = tag('div', {}, [
        tag('p', {}, [
            'Answers must be submitted in this exact order',
            tag('input', { 'type': 'checkbox', 'name': 'ordered' }, [])
        ]),
        tag('p', {}, 'Provide up to ten valid answers (case-insensitive):'),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer1' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer2' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer3' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer4' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer5' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer6' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer7' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer8' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer9' }, []),
        tag('br', {}, []),
        tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer10' }, []),
        tag('br', {}, [])
    ]);

    return answerInputs;
}

/*
 * Renders answer input for Matching type questions.
 *
 * Usage for handling post requests:
 *
 * String answer1-begin = request.getParameter("answer1-begin");
 * String answer1-end = request.getParameter("answer1-end");
 *
 * String answer1Text = answer1-begin + "===" + answer1-end; // use "===" as delimiter
 *
 * Make sure to check if the answer is null (user left one or more inputs blank). 
 * All non-null answers should be created as answer objects with valid=true:
 *
 * Answer ans1 = new Answer(answer1Text, questionNumber, quiz_id, true);
 */
function renderMatching() {

    var answerInputs = tag('div', {}, [
        tag('p', {}, 'Provide up to four pairs of valid matches:'),
        tag('p', {}, [
            tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer1-begin' }, []),
            ' matches with ',
            tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer1-end' }, []),
        ]),
        tag('p', {}, [
            tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer2-begin' }, []),
            ' matches with ',
            tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer2-end' }, []),
        ]),
        tag('p', {}, [
            tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer3-begin' }, []),
            ' matches with ',
            tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer3-end' }, []),
        ]),
        tag('p', {}, [
            tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer4-begin' }, []),
            ' matches with ',
            tag('input', { 'type': 'text', 'class': 'answers', 'name': 'answer4-end' }, []),
        ]),
    ]);

    return answerInputs;
}