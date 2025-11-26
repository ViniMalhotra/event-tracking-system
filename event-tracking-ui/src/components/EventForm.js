import React, { useState } from 'react';
import '../styles/EventForm.css';

const EventForm = ({ onSubmit, initialData = null, onCancel }) => {
    const [formData, setFormData] = useState(initialData || {
        name: '',
        description: '',
        startDate: '',
        endDate: '',
        location: '',
        minAttendees: '',
        maxAttendees: '',
        locationNotes: '',
        preparationNotes: ''
    });

    const [errors, setErrors] = useState({});

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const validateForm = () => {
        const newErrors = {};

        if (!formData.name.trim()) newErrors.name = 'Event name is required';
        if (!formData.description.trim()) newErrors.description = 'Description is required';
        if (!formData.startDate) newErrors.startDate = 'Start date is required';
        if (!formData.endDate) newErrors.endDate = 'End date is required';
        if (!formData.location.trim()) newErrors.location = 'Location is required';

        if (formData.startDate && formData.endDate) {
            const start = new Date(formData.startDate);
            const end = new Date(formData.endDate);
            if (start >= end) {
                newErrors.dates = 'Start date must be before end date';
            }
        }

        // Check attendee constraints - min should not exceed max
        if (formData.minAttendees && formData.maxAttendees) {
            const min = parseInt(formData.minAttendees);
            const max = parseInt(formData.maxAttendees);
            if (min > max) {
                newErrors.attendees = 'Minimum attendees cannot exceed maximum';
            }
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (validateForm()) {
            onSubmit(formData);
        }
    };

    return (
        <form className="event-form" onSubmit={handleSubmit}>
            <div className="form-group">
                <label htmlFor="name">Event Name *</label>
                <input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    placeholder="Enter event name"
                />
                {errors.name && <span className="error">{errors.name}</span>}
            </div>

            <div className="form-group">
                <label htmlFor="description">Description *</label>
                <textarea
                    id="description"
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    placeholder="Enter event description"
                    rows="3"
                />
                {errors.description && <span className="error">{errors.description}</span>}
            </div>

            <div className="form-row">
                <div className="form-group">
                    <label htmlFor="startDate">Start Date *</label>
                    <input
                        type="datetime-local"
                        id="startDate"
                        name="startDate"
                        value={formData.startDate}
                        onChange={handleChange}
                    />
                    {errors.startDate && <span className="error">{errors.startDate}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="endDate">End Date *</label>
                    <input
                        type="datetime-local"
                        id="endDate"
                        name="endDate"
                        value={formData.endDate}
                        onChange={handleChange}
                    />
                    {errors.endDate && <span className="error">{errors.endDate}</span>}
                </div>
            </div>
            {errors.dates && <span className="error">{errors.dates}</span>}

            <div className="form-group">
                <label htmlFor="location">Location *</label>
                <input
                    type="text"
                    id="location"
                    name="location"
                    value={formData.location}
                    onChange={handleChange}
                    placeholder="Enter event location"
                />
                {errors.location && <span className="error">{errors.location}</span>}
            </div>

            <div className="form-row">
                <div className="form-group">
                    <label htmlFor="minAttendees">Min Attendees</label>
                    <input
                        type="number"
                        id="minAttendees"
                        name="minAttendees"
                        value={formData.minAttendees}
                        onChange={handleChange}
                        placeholder="Minimum attendees"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="maxAttendees">Max Attendees</label>
                    <input
                        type="number"
                        id="maxAttendees"
                        name="maxAttendees"
                        value={formData.maxAttendees}
                        onChange={handleChange}
                        placeholder="Maximum attendees"
                    />
                </div>
            </div>

            <div className="form-group">
                <label htmlFor="locationNotes">Location Notes</label>
                <textarea
                    id="locationNotes"
                    name="locationNotes"
                    value={formData.locationNotes}
                    onChange={handleChange}
                    placeholder="Additional location information"
                    rows="2"
                />
            </div>

            <div className="form-group">
                <label htmlFor="preparationNotes">Preparation Notes</label>
                <textarea
                    id="preparationNotes"
                    name="preparationNotes"
                    value={formData.preparationNotes}
                    onChange={handleChange}
                    placeholder="Preparation instructions"
                    rows="2"
                />
            </div>

            <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                    {initialData ? 'Update Event' : 'Create Event'}
                </button>
                <button type="button" className="btn btn-secondary" onClick={onCancel}>
                    Cancel
                </button>
            </div>
        </form>
    );
};

export default EventForm;
