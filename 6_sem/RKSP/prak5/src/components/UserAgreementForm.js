import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { toggleAgreement } from '../redux/actions';

const UserAgreementForm = () => {
    const agreementAccepted = useSelector(state => state.agreementAccepted);
    const dispatch = useDispatch();

    const handleCheckboxChange = () => {
        dispatch(toggleAgreement());
    };

    const handleSubmit = (e) => {
        e.preventDefault();
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                <input
                    type="checkbox"
                    checked={agreementAccepted}
                    onChange={handleCheckboxChange}
                />
                Я согласен с условиями пользовательского соглашения
            </label>
            <button type="submit" disabled={!agreementAccepted}>
                Подтвердить соглашение
            </button>
        </form>
    );
};

export default UserAgreementForm;
